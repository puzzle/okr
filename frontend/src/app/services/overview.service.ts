import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EMPTY, map, Observable, tap, finalize } from 'rxjs';
import { Params } from '@angular/router';
import { optionalValue, getValueFromQuery, getQueryString } from '../shared/common';
import { State } from '../shared/types/enums/state';
import { OverviewEntity } from '../shared/types/model/overview-entity';
import { FilterPageChange } from '../shared/types/model/filter-page-change';

@Injectable({
  providedIn: 'root'
})
export class OverviewService {
  private http = inject(HttpClient);

  private lastFilters: FilterPageChange | null = null;

  private _data = signal<OverviewEntity[] | null>(null);

  private _loading = signal(false);

  public readonly data = this._data.asReadonly();

  public readonly loading = this._loading.asReadonly();

  load(params: Params) {
    const filters = this.mapParamsToFilters(params);
    this.lastFilters = filters;
    return this.fetch(filters);
  }

  reload() {
    if (!this.lastFilters) {
      return EMPTY;
    }
    return this.fetch(this.lastFilters)
      .subscribe();
  }

  private mapParamsToFilters(params: Params): FilterPageChange {
    return {
      quarterId: getValueFromQuery(params['quarter'])[0],
      teamIds: getValueFromQuery(params['teams']),
      objectiveQueryString: getQueryString(params['objectiveQuery'])
    };
  }

  private fetch(filters: FilterPageChange) {
    this._loading.set(true);

    return this.getOverview(filters.quarterId, filters.teamIds, filters.objectiveQueryString)
      .pipe(tap((data) => this._data.set(data)), finalize(() => this._loading.set(false)));
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
