import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Team } from '../shared/types/model/Team';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User } from '../shared/types/model/User';
import { UserTeam } from '../shared/types/model/UserTeam';

@Injectable({
  providedIn: 'root',
})
export class TeamService {
  constructor(private http: HttpClient) {}

  private teams: BehaviorSubject<Team[]> = new BehaviorSubject<Team[]>([]);
  private teamsLoaded = false;
  private readonly API_URL = '/api/v2/teams';

  getAllTeams(): Observable<Team[]> {
    if (!this.teamsLoaded) {
      this.reloadTeams();
      this.teamsLoaded = true;
    }
    return this.teams.asObservable();
  }

  reloadTeams(): void {
    this.http.get<Team[]>(this.API_URL).pipe(
      tap((teams) => {
        if (!this.teams) {
          this.teams = new BehaviorSubject<Team[]>(teams);
          return;
        }
        this.teams.next(teams);
      }),
    );
  }

  createTeam(team: Team): Observable<Team> {
    return this.http.post<Team>(this.API_URL, team).pipe(tap(() => this.reloadTeams()));
  }

  updateTeam(team: Team): Observable<Team> {
    return this.http.put<Team>(`${this.API_URL}/${team.id}`, team).pipe(tap(() => this.reloadTeams()));
  }

  deleteTeam(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`).pipe(tap(() => this.reloadTeams()));
  }

  addUsersToTeam(team: Team, selectedUsers: User[]): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${team.id}/addusers`, selectedUsers);
  }

  removeUserFromTeam(userId: number, team: Team): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${team.id}/user/${userId}/removeuser`, null);
  }

  updateOrAddTeamMembership(user: User, userTeam: UserTeam): Observable<void> {
    return this.http.put<void>(
      `${this.API_URL}/${userTeam.team.id}/user/${user.id}/updateaddteammembership/${userTeam.isTeamAdmin}`,
      {},
    );
  }
}
