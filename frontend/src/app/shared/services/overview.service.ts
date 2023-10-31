import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { OverviewEntity } from '../types/model/OverviewEntity';
import { State } from '../types/enums/State';
import { optionalValue } from '../common';
import { FilterModel } from '../types/model/FilterModel';

@Injectable({
  providedIn: 'root',
})
export class OverviewService {
  constructor(private http: HttpClient) {}

  getOverview(filterValue?: FilterModel): Observable<OverviewEntity[]> {
    const params = optionalValue({
      quarter: filterValue?.quarterId,
      team: filterValue?.teamIds,
      objectiveQuery: filterValue?.objectiveQuery,
    });

    return this.http.get<OverviewEntity[]>('/api/v2/overview', { params: params }).pipe(
      //Map state from string to enum
      map((overviews) => {
        overviews.forEach((overview) => {
          overview.objectives.forEach((objective) => {
            objective.state = State[objective.state as string as keyof typeof State];
            return objective;
          });
          return overview;
        });
        return overviews;
      }),
    );
  }
}
