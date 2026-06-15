import { Component, Input, inject, input, computed } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { UserTableEntry } from '../../../shared/types/model/user-table-entry';
import { User } from '../../../shared/types/model/user';
import { Team } from '../../../shared/types/model/team';
import { TeamStateService } from '../../../services/team.state.service';
import { UserService } from '../../../services/user.service';
import { getRouteToUserDetails } from '../../../shared/route-utils';
import { filter, mergeMap } from 'rxjs';
import { UserTeam } from '../../../shared/types/model/user-team';
import { DialogService } from '../../../services/dialog.service';

@Component({
  selector: 'app-member-list-table',
  templateUrl: './member-list-table.component.html',
  styleUrl: './member-list-table.component.scss',
  standalone: false
})

export class MemberListTableComponent {
  private readonly teamStateService = inject(TeamStateService);

  private readonly userService = inject(UserService);

  private readonly dialogService = inject(DialogService);

  @Input({ required: true }) dataSource!: MatTableDataSource<UserTableEntry>;

  currentTeam = input<Team>();

  private allColumns = [
    'icon',
    'name',
    'roles',
    'teams',
    'okr_champion'
  ];

  private teamColumns = ['icon',
    'name',
    'role'];

  displayedColumns = computed(() => {
    const team = this.currentTeam();
    if (team) {
      const cols = [...this.teamColumns];
      if (team.isWriteable) {
        cols.push('menu');
      }
      return cols;
    }
    return this.allColumns;
  });

  isTeamWriteable = computed(() => !!this.currentTeam()?.isWriteable);

  isTeamArchived = computed(() => !!this.currentTeam()?.markedAsArchivedAt);

  removeMemberFromTeam(entry: UserTableEntry, event: MouseEvent) {
    event.stopPropagation();
    event.preventDefault();

    const team = this.currentTeam();
    if (!team) {
      return;
    }

    const i18nData = {
      user: `${entry.firstName} ${entry.lastName}`,
      team: team.name
    };

    this.dialogService
      .openConfirmDialog('CONFIRMATION.DELETE.USER_FROM_TEAM', i18nData)
      .afterClosed()
      .pipe(filter((confirm) => confirm), mergeMap(() => this.teamStateService.removeUserFromTeam(entry.id, team)))
      .subscribe(() => {
        this.userService.reloadUsers();
        this.userService.reloadCurrentUser()
          .subscribe();
      });
  }

  saveUserTeamMembership(isAdmin: boolean, userTableEntry: UserTableEntry, userTeam: UserTeam): void {
    const newUserTeam = { ...userTeam };
    newUserTeam.isTeamAdmin = isAdmin;

    this.teamStateService.updateOrAddTeamMembership(userTableEntry.id, newUserTeam)
      .subscribe(() => {
        userTeam.isTeamAdmin = isAdmin;
        this.userService.reloadUsers();
        this.userService.reloadCurrentUser()
          .subscribe();
      });
  }

  getMemberDetailsLink(user: User, team?: Team) {
    return getRouteToUserDetails(user.id, team?.id);
  }

  getSingleUserTeam(userTableEntry: UserTableEntry): UserTeam {
    if (userTableEntry.userTeamList.length !== 1) {
      throw Error('it should have exactly one UserTeam at this point');
    }
    return userTableEntry.userTeamList[0];
  }
}
