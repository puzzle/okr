import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Team } from '../shared/types/model/Team';
import { Observable, of, take, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TeamService {
  constructor(private http: HttpClient) {}

  private teams: Team[] | undefined;

  getAllTeams(): Observable<Team[]> {
    if (this.teams) {
      return of(this.teams).pipe(take(1));
    }
    return this.http.get<Team[]>('/api/v2/teams').pipe(tap((teams) => (this.teams = teams)));
  }

  reloadTeams(): Observable<Team[]> {
    this.teams = undefined;
    return this.getAllTeams();
  }

  createTeam(team: Team): Observable<Team> {
    return this.http.post<Team>('/api/v2/teams', team);
  }

  updateTeam(team: Team): Observable<Team> {
    return this.http.put<Team>(`/api/v2/teams/${team.id}`, team);
  }

  deleteTeam(id: number): Observable<any> {
    return this.http.delete(`/api/v2/teams/${id}`);
  }
}
