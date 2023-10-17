import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TeamMin } from '../types/model/TeamMin';
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

  getTeamIdsFromQuery(query: any): number[] {
    return Array.from([query])
      .flat(1)
      .map((id: any) => Number(id))
      .filter((id: number) => Number.isInteger(id));
  }
}
