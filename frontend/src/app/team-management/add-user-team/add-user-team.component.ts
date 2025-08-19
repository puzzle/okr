import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, inject } from '@angular/core';
import { Team } from '../../shared/types/model/team';
import { TeamService } from '../../services/team.service';
import { combineLatest, map, Observable, Subject, takeUntil } from 'rxjs';
import { UserTeam } from '../../shared/types/model/user-team';

@Component({
  selector: 'app-add-user-team',
  templateUrl: './add-user-team.component.html',
  styleUrl: './add-user-team.component.scss',
  standalone: false
})
export class AddUserTeamComponent implements OnInit, OnDestroy {
  private readonly teamService = inject(TeamService);

  @Output()
  addUserTeam = new EventEmitter<UserTeam>();

  @Input({ required: true })
  currentTeams$!: Observable<UserTeam[]>;

  userTeam: { team: Team;
    isTeamAdmin: boolean; } | undefined;

  selectableAdminTeams$: Observable<Team[]> | undefined;

  allAdminTeams$: Observable<Team[]> | undefined;

  private readonly unsubscribe$ = new Subject<void>();

  ngOnInit() {
    this.allAdminTeams$ = this.teamService.getAllTeams()
      .pipe(takeUntil(this.unsubscribe$), map((teams) => {
        return teams.filter((t) => t.isWriteable);
      }));

    this.selectableAdminTeams$ = combineLatest([this.allAdminTeams$,
      this.currentTeams$])
      .pipe(takeUntil(this.unsubscribe$), map(([allTeams,
        userTeams]) => {
        const currentTeamIds = userTeams.map((ut) => ut.team.id);
        return allTeams.filter((t) => {
          return !currentTeamIds.includes(t.id);
        });
      }));
  }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  createUserTeam(team: Team) {
    this.userTeam = {
      team,
      isTeamAdmin: false
    };
  }

  save(): void {
    if (!this.userTeam) {
      throw new Error('UserTeam should be defined here');
    }
    this.addUserTeam.next(this.userTeam);
    this.userTeam = undefined;
  }

  isAddButtonVisible(adminTeams: Team[] | null) {
    return !this.userTeam && adminTeams?.length;
  }

  isAddButtonDisabled(selectableAdminTeams: Team[] | null) {
    return !selectableAdminTeams?.length;
  }
}
