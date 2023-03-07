import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Objective } from './objective.service';
import { Team } from './team.service';
import { HttpClient, HttpParams } from '@angular/common/http';

export interface Overview {
  team: Team;
  objectives: Objective[];
}

@Injectable({
  providedIn: 'root',
})
export class OverviewService {
  constructor(private httpClient: HttpClient) {}

  public getOverview(quarterFilter: number | undefined, teamFilter: number[]): Observable<Overview[]> {
    let params = new HttpParams();
    if (quarterFilter !== undefined) {
      params = params.append('quarter', quarterFilter);
    }
    if (teamFilter.length !== 0) {
      params = params.append('team', teamFilter.toString());
    }
    return this.httpClient.get<Overview[]>('api/v1/overview', { params });
  }
}
