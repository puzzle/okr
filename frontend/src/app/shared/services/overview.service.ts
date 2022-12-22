import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Objective } from './objective.service';
import { Team } from './team.service';
import { HttpClient } from '@angular/common/http';

export interface Overview {
  team: Team;
  objectives: Objective[];
}

@Injectable({
  providedIn: 'root',
})
export class OverviewService {
  constructor(private httpClient: HttpClient) {}

  public getOverview(): Observable<Overview[]> {
    return this.httpClient.get<Overview[]>('api/v1/overview');
  }
}
