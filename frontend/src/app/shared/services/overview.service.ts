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

  public getOverview(
    quarterFilter: number | undefined,
    teamFilter: number[]
  ): Observable<Overview[]> {
    let params = new HttpParams();
    if (quarterFilter !== undefined) {
      // if no filter is set manually the console.log should always show 1 in order to get the correct overview
      console.log(
        '(7): the current quarter Filter in get overview ==>',
        quarterFilter
      );
      params = params.append('quarter', quarterFilter);
    }
    if (teamFilter.length !== 0) {
      params = params.append('team', teamFilter.toString());
    }
    return this.httpClient.get<Overview[]>('api/v1/overview', { params });
  }
}
