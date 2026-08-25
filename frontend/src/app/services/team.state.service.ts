import { Injectable, inject, Signal, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Team } from '../shared/types/model/team';
import { User } from '../shared/types/model/user';
import { UserTeam } from '../shared/types/model/user-team';
import { TeamService, TeamFilters } from './team.service';
import { UserService } from './user.service';

@Injectable()
export class TeamStateService {
  private teamService = inject(TeamService);

  private userService = inject(UserService);

  private teams = signal<Team[]>([]);

  private activeFilters: TeamFilters = {};

  getTeams(): Signal<Team[]> {
    return this.teams.asReadonly();
  }

  loadTeams(filters: TeamFilters = {}): Observable<Team[]> {
    this.activeFilters = filters;
    return this.reload();
  }

  reload(): Observable<Team[]> {
    return this.teamService.getAllTeams(this.activeFilters)
      .pipe(tap((teams) => this.teams.set(teams)));
  }

  createTeam(team: Team): Observable<Team> {
    return this.teamService.createTeam(team)
      .pipe(tap(() => this.reload()
        .subscribe()));
  }

  updateTeam(team: Team): Observable<Team> {
    return this.teamService.updateTeam(team)
      .pipe(tap(() => this.reload()
        .subscribe()));
  }

  deleteTeam(id: number): Observable<void> {
    return this.teamService.deleteTeam(id)
      .pipe(tap(() => this.reload()
        .subscribe()));
  }

  addUsersToTeam(team: Team, selectedUsers: User[]): Observable<void> {
    return this.teamService.addUsersToTeam(team, selectedUsers)
      .pipe(tap(() => this.reload()
        .subscribe()));
  }

  removeUserFromTeam(userId: number, team: Team): Observable<void> {
    return this.teamService.removeUserFromTeam(userId, team)
      .pipe(tap(() => this.reload()
        .subscribe()));
  }

  updateOrAddTeamMembership(userId: number, userTeam: UserTeam): Observable<void> {
    return this.teamService.updateOrAddTeamMembership(userId, userTeam)
      .pipe(tap(() => this.reload()
        .subscribe()));
  }

  archiveTeam(team: Team): Observable<void> {
    return this.teamService.archiveTeam(team)
      .pipe(tap(() => {
        this.reload()
          .subscribe();
        this.userService.reloadUsers();
      }));
  }

  unarchiveTeam(id: number): Observable<void> {
    return this.teamService.unarchiveTeam(id)
      .pipe(tap(() => {
        this.reload()
          .subscribe();
        this.userService.reloadUsers();
      }));
  }
}
