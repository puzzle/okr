import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Team } from '../types/model/Team';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TeamService {
  constructor(private http: HttpClient) {}

  getAllTeams(): Observable<Team[]> {
    return this.http.get<Team[]>('/api/v2/teams');
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
