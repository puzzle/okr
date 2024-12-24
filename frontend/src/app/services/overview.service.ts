import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { optionalValue } from '../shared/common';
import { State } from '../shared/types/enums/state';
import { OverviewEntity } from '../shared/types/model/overview-entity';

@Injectable({
  providedIn: 'root'
})
export class OverviewService {
  constructor(private http: HttpClient) {}

  getOverview(quarterId?: number, teamIds?: number[], objectiveQuery?: string): Observable<OverviewEntity[]> {
    const params = optionalValue({
      quarter: quarterId,
      team: teamIds,
      objectiveQuery: objectiveQuery
    });
    return this.http.get<OverviewEntity[]>('/api/v2/overview', { params: params })
      .pipe(map((overviews) => {
        overviews.forEach((overview) => {
          overview.objectives.forEach((objective) => {
            objective.state = State[objective.state as string as keyof typeof State];
            return objective;
          });
        });
        return overviews;
      }));
  }
}
