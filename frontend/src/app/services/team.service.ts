import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Team } from '../shared/types/model/team';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User } from '../shared/types/model/user';
import { UserTeam } from '../shared/types/model/user-team';

export interface TeamFilters {
  quarterId?: number;
}

@Injectable()
export class TeamService {
  private http = inject(HttpClient);

  private readonly API_URL = '/api/v2/teams';

  private teams$ = new BehaviorSubject<Team[]>([]);

  private activeFilters: TeamFilters = {};

  getTeams(): Observable<Team[]> {
    return this.teams$.asObservable();
  }

  loadTeams(filters: TeamFilters = {}): void {
    this.activeFilters = filters;
    this.reload();
  }

  reload(): void {
    let params = new HttpParams();

    if (this.activeFilters.quarterId !== undefined && this.activeFilters.quarterId !== null) {
      params = params.set('quarterId', this.activeFilters.quarterId.toString());
    }

    this.http.get<Team[]>(this.API_URL, { params })
      .subscribe((teams) => this.teams$.next(teams));
  }

  createTeam(team: Team): Observable<Team> {
    return this.http.post<Team>(this.API_URL, team)
      .pipe(tap(() => this.reload()));
  }

  updateTeam(team: Team): Observable<Team> {
    return this.http.put<Team>(`${this.API_URL}/${team.id}`, team)
      .pipe(tap(() => this.reload()));
  }

  deleteTeam(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`)
      .pipe(tap(() => this.reload()));
  }

  addUsersToTeam(team: Team, selectedUsers: User[]): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${team.id}/addusers`, selectedUsers);
  }

  removeUserFromTeam(userId: number, team: Team): Observable<void> {
    return this.http
      .put<void>(`${this.API_URL}/${team.id}/user/${userId}/removeuser`, null)
      .pipe(tap(() => this.reload()));
  }

  updateOrAddTeamMembership(userId: number, userTeam: UserTeam): Observable<void> {
    return this.http
      .put<void>(`${this.API_URL}/${userTeam.team.id}/user/${userId}/updateaddteammembership/${userTeam.isTeamAdmin}`, {})
      .pipe(tap(() => this.reload()));
  }

  archiveTeam(team: Team): Observable<void> {
    const payload = { markedAsArchivedAt: team.markedAsArchivedAt };
    return this.http.put<void>(`${this.API_URL}/${team.id}/archive`, payload)
      .pipe(tap(() => this.reload()));
  }

  unarchiveTeam(id: number): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${id}/unarchive`, null)
      .pipe(tap(() => this.reload()));
  }
}
