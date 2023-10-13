import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { OverviewEntity } from '../types/model/OverviewEntity';
import { State } from '../types/enums/State';

@Injectable({
  providedIn: 'root',
})
export class OverviewService {
  constructor(private http: HttpClient) {}

  getOverview(quarterId?: number): Observable<OverviewEntity[]> {
    return this.http
      .get<OverviewEntity[]>(quarterId !== undefined ? '/api/v2/overview?quarter=' + quarterId : '/api/v2/overview')
      .pipe(
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
