import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Team } from '../shared/types/model/team';
import { User } from '../shared/types/model/user';
import { UserTeam } from '../shared/types/model/user-team';

export interface TeamFilters {
  quarterId?: number;
}

@Injectable({
  providedIn: 'root'
})

export class TeamService {
  private http = inject(HttpClient);

  private readonly API_URL = '/api/v2/teams';

  getAllTeams(filters: TeamFilters = {}): Observable<Team[]> {
    let params = new HttpParams();
    if (filters.quarterId != null && !Number.isNaN(Number(filters.quarterId))) {
      params = params.set('quarterId', filters.quarterId.toString());
    }
    return this.http.get<Team[]>(this.API_URL, { params });
  }

  createTeam(team: Team): Observable<Team> {
    return this.http.post<Team>(this.API_URL, team);
  }

  updateTeam(team: Team): Observable<Team> {
    return this.http.put<Team>(`${this.API_URL}/${team.id}`, team);
  }

  deleteTeam(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  addUsersToTeam(team: Team, selectedUsers: User[]): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${team.id}/addusers`, selectedUsers);
  }

  removeUserFromTeam(userId: number, team: Team): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${team.id}/user/${userId}/removeuser`, {});
  }

  updateOrAddTeamMembership(userId: number, userTeam: UserTeam): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${userTeam.team.id}/user/${userId}/updateaddteammembership/${userTeam.isTeamAdmin}`, {});
  }

  archiveTeam(team: Team): Observable<void> {
    const payload = { markedAsArchivedAt: team.markedAsArchivedAt };
    return this.http.put<void>(`${this.API_URL}/${team.id}/archive`, payload);
  }

  unarchiveTeam(id: number): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${id}/unarchive`, {});
  }
}
