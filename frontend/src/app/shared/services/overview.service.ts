import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { OverviewEntity } from '../types/model/OverviewEntity';
import { State } from '../types/enums/State';
import { optional } from '../common';

@Injectable({
  providedIn: 'root',
})
export class OverviewService {
  constructor(private http: HttpClient) {}

  getOverview(quarterId?: number, teamIds?: number[]): Observable<OverviewEntity[]> {
    const params = optional({
      quarter: quarterId,
      team: teamIds,
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
