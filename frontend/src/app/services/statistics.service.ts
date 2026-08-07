import { inject, Injectable, signal } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { Params } from '@angular/router';
import { FilterPageChange } from '../shared/types/model/filter-page-change';
import { getQueryString, getValueFromQuery } from '../shared/common';
import { catchError, EMPTY, Observable, of } from 'rxjs';
import { EvaluationService } from './evaluation.service';
import { Statistics } from '../shared/types/model/statistics';

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {
  private readonly filter = signal<FilterPageChange | null>(null);

  private readonly evaluationService = inject(EvaluationService);

  activeFilter: FilterPageChange | undefined;

  statisticsResource = rxResource({
    params: () => this.filter(),
    stream: ({ params: filters }) => {
      if (!filters) {
        return of(null);
      }
      return this.loadOverview(filters);
    }
  });

  public readonly data = this.statisticsResource.value;

  loadOverview(filterPage: FilterPageChange): Observable<Statistics> {
    this.activeFilter = filterPage;
    return this.evaluationService
      .getStatistics(filterPage.quarterId, filterPage.teamIds)
      .pipe(catchError(() => {
        return EMPTY;
      }));
  }

  load(params: Params) {
    this.filter.set(this.mapParamsToFilters(params));
  }

  reload() {
    this.statisticsResource.reload();
  }

  private mapParamsToFilters(params: Params): FilterPageChange {
    return {
      quarterId: getValueFromQuery(params['quarter'])[0],
      teamIds: getValueFromQuery(params['teams']),
      objectiveQueryString: getQueryString(params['objectiveQuery'])
    };
  }
}
