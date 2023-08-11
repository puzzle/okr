import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Team {
  id: number | undefined;
  name: string;
}

@Injectable({
  providedIn: 'root',
})
export class TeamService {
  constructor(private httpClient: HttpClient) {}

  public getTeams(): Observable<Team[]> {
    return this.httpClient.get<Team[]>('api/v1/teams');
  }
  public getTeam(teamId: number): Observable<Team> {
    return this.httpClient.get<Team>('api/v1/teams/' + teamId);
  }

  public save(team: Team): Observable<Team> {
    if (team.id === undefined) {
      return this.httpClient.post<Team>('api/v1/teams', team);
    } else {
      return this.httpClient.put<Team>('api/v1/teams/' + team.id, team);
    }
  }

  public deleteTeamById(teamId: number): Observable<Team> {
    return this.httpClient.delete<Team>('api/v1/teams/' + teamId);
  }

  getInitTeam() {
    return {
      id: undefined,
      name: '',
    };
  }
}
