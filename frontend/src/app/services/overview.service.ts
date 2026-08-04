import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, of } from 'rxjs';
import { Params } from '@angular/router';
import { optionalValue, getValueFromQuery, getQueryString } from '../shared/common';
import { State } from '../shared/types/enums/state';
import { OverviewEntity } from '../shared/types/model/overview-entity';
import { FilterPageChange } from '../shared/types/model/filter-page-change';
import { rxResource } from '@angular/core/rxjs-interop';

@Injectable({
  providedIn: 'root'
})
export class OverviewService {
  private readonly http = inject(HttpClient);

  private readonly filter = signal<FilterPageChange | null>(null);

  overviewResource = rxResource({
    params: () => this.filter(),
    stream: ({ params: filters }) => {
      if (!filters) {
        return of(null);
      }
      return this.getOverview(filters.quarterId, filters.teamIds, filters.objectiveQueryString);
    }
  });

  public readonly data = this.overviewResource.value;

  load(params: Params) {
    this.filter.set(this.mapParamsToFilters(params));
  }

  reload() {
    this.overviewResource.reload();
  }

  private mapParamsToFilters(params: Params): FilterPageChange {
    return {
      quarterId: getValueFromQuery(params['quarter'])[0],
      teamIds: getValueFromQuery(params['teams']),
      objectiveQueryString: getQueryString(params['objectiveQuery'])
    };
  }

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
