import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Team } from '../shared/types/model/team';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User } from '../shared/types/model/user';
import { UserTeam } from '../shared/types/model/user-team';

@Injectable({
  providedIn: 'root'
})
export class TeamService {
  private http = inject(HttpClient);

  private activeQuarterId?: number;

  private allTeams = new BehaviorSubject<Team[]>([]);

  private quarterTeams = new BehaviorSubject<Team[]>([]);

  private readonly API_URL = '/api/v2/teams';

  private teamsLoaded = false;

  getAllTeams() {
    if (!this.teamsLoaded) {
      this.reloadState();
      this.teamsLoaded = true;
    }
    return this.allTeams.asObservable();
  }

  getQuarterTeams() {
    return this.quarterTeams.asObservable();
  }

  loadTeamsForQuarter(quarterId?: number) {
    this.activeQuarterId = quarterId;
    this.reloadQuarterTeams();
  }

  reloadState() {
    this.http.get<Team[]>(this.API_URL)
      .subscribe((teams) => this.allTeams.next(teams));

    if (this.activeQuarterId) {
      this.reloadQuarterTeams();
    }
  }

  private reloadQuarterTeams() {
    let params = new HttpParams()
      .set('quarterId', this.activeQuarterId!);

    if (this.activeQuarterId != null) {
      params = params.set('quarterId', this.activeQuarterId.toString());
    }

    this.http.get<Team[]>(this.API_URL, { params })
      .subscribe((teams) => this.quarterTeams.next(teams));
  }

  createTeam(team: Team): Observable<Team> {
    return this.http.post<Team>(this.API_URL, team)
      .pipe(tap(() => this.reloadState()));
  }

  updateTeam(team: Team): Observable<Team> {
    return this.http.put<Team>(`${this.API_URL}/${team.id}`, team)
      .pipe(tap(() => this.reloadState()));
  }

  deleteTeam(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`)
      .pipe(tap(() => this.reloadState()));
  }

  addUsersToTeam(team: Team, selectedUsers: User[]): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${team.id}/addusers`, selectedUsers);
  }

  removeUserFromTeam(userId: number, team: Team): Observable<void> {
    return this.http
      .put<void>(`${this.API_URL}/${team.id}/user/${userId}/removeuser`, null)
      .pipe(tap(() => this.reloadState()));
  }

  updateOrAddTeamMembership(userId: number, userTeam: UserTeam): Observable<void> {
    return this.http
      .put<void>(`${this.API_URL}/${userTeam.team.id}/user/${userId}/updateaddteammembership/${userTeam.isTeamAdmin}`, {})
      .pipe(tap(() => this.reloadState()));
  }

  archiveTeam(team: Team): Observable<void> {
    const payload = {
      markedAsArchivedAt: team.markedAsArchivedAt
    };

    return this.http.put<void>(`${this.API_URL}/${team.id}/archive`, payload)
      .pipe(tap(() => this.reloadState()));
  }

  unarchiveTeam(id: number): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${id}/unarchive`, null)
      .pipe(tap(() => this.reloadState()));
  }
}
