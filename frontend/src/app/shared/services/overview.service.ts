import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { optionalValue } from '../common';
import { Dashboard } from '../types/model/Dashboard';
import { State } from '../types/enums/State';

@Injectable({
  providedIn: 'root',
})
export class OverviewService {
  constructor(private http: HttpClient) {}

  getOverview(quarterId?: number, teamIds?: number[], objectiveQuery?: string): Observable<Dashboard> {
    const params = optionalValue({
      quarter: quarterId,
      team: teamIds,
      objectiveQuery: objectiveQuery,
    });
    return this.http.get<Dashboard>('/api/v2/overview', { params: params }).pipe(
      map((dashboard) => {
        let overviews = dashboard.overviews;
        overviews.forEach((overview) => {
          overview.objectives.forEach((objective) => {
            objective.state = State[objective.state as string as keyof typeof State];
            return objective;
          });
          return dashboard;
        });
        return dashboard;
      }),
    );
  }
}
