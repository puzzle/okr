import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { UserTableEntry } from '../../../shared/types/model/UserTableEntry';
import { User } from '../../../shared/types/model/User';
import { Team } from '../../../shared/types/model/Team';
import { TeamService } from '../../../services/team.service';
import { UserService } from '../../../services/user.service';
import { getRouteToUserDetails } from '../../../shared/routeUtils';
import { BehaviorSubject, Subject, takeUntil } from 'rxjs';
import { UserTeam } from '../../../shared/types/model/UserTeam';

@Component({
  selector: 'app-member-list-table',
  templateUrl: './member-list-table.component.html',
  styleUrl: './member-list-table.component.scss',
})
export class MemberListTableComponent implements OnInit, OnDestroy {
  @Input({ required: true }) dataSource!: MatTableDataSource<UserTableEntry>;
  @Input({ required: true }) selectedTeam$!: BehaviorSubject<undefined | Team>;

  private allColumns = ['icon', 'name', 'roles', 'teams', 'okr_champion'];
  private teamColumns = ['icon', 'name', 'role'];
  private unsubscribe$ = new Subject<void>();

  displayedColumns: string[] = this.allColumns;

  constructor(
    private readonly teamService: TeamService,
    private readonly userService: UserService,
  ) {}

  ngOnInit() {
    this.selectedTeam$.pipe(takeUntil(this.unsubscribe$)).subscribe((team) => {
      team ? this.setColumnForTeam(team) : this.setColumnsForAllTeams();
    });
  }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  private setColumnForTeam(team: Team) {
    this.displayedColumns = [...this.teamColumns];
    if (team.writeable) {
      this.displayedColumns.push('menu');
    }
  }

  private setColumnsForAllTeams() {
    this.displayedColumns = this.allColumns;
  }

  removeMemberFromTeam(entry: UserTableEntry, event: MouseEvent) {
    event.stopPropagation();
    event.preventDefault();
    this.teamService.removeUserFromTeam(entry.id, this.selectedTeam$.value!).subscribe(() => {
      this.userService.reloadUsers();
      this.userService.reloadCurrentUser().subscribe();
    });
  }

  saveUserTeamRole(userTableEntry: UserTableEntry, userTeam: UserTeam): void {
    this.teamService.updateOrAddTeamMembership(userTableEntry.id, userTeam).subscribe(() => {
      this.userService.reloadUsers();
      this.userService.reloadCurrentUser().subscribe();
    });
  }

  getMemberDetailsLink(user: User, team?: Team) {
    return getRouteToUserDetails(user.id, team?.id);
  }

  // this method is only used in Team context. Therefore, it should only have one userTeam.
  // otherwise the method will throw an exception
  getSingleUserTeam(userTableEntry: UserTableEntry): UserTeam {
    if (userTableEntry.userTeamList.length !== 1) {
      throw Error('it should have exactly one UserTeam at this point');
    }
    return userTableEntry.userTeamList[0];
  }
}
