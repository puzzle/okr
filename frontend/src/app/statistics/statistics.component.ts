import { Component } from '@angular/core';
import { catchError, combineLatest, EMPTY, Observable, take } from 'rxjs';
import { RefreshDataService } from '../services/refresh-data.service';
import { ActivatedRoute } from '@angular/router';
import { getValueFromQuery } from '../shared/common';
import { EvaluationService } from '../services/evaluation.service';
import { Statistics } from '../shared/types/model/statistics';

@Component({
  selector: 'app-statistics',
  standalone: false,
  templateUrl: './statistics.component.html',
  styleUrl: './statistics.component.scss'
})
export class StatisticsComponent {
  statistics = new Observable<Statistics>();

  constructor(private refreshDataService: RefreshDataService, private activatedRoute: ActivatedRoute, private evaluationService: EvaluationService) {
    this.refreshDataService.reloadOverviewSubject
    // .pipe(takeUntilDestroyed())
      .subscribe(() => this.loadOverviewWithParams());

    combineLatest([refreshDataService.teamFilterReady.asObservable(),
      refreshDataService.quarterFilterReady.asObservable()])
      .pipe(take(1))
      .subscribe(() => {
        this.activatedRoute.queryParams
        // .pipe(takeUntilDestroyed())
          .subscribe(() => {
            this.loadOverviewWithParams();
          });
      });
  }

  loadOverviewWithParams() {
    const quarterQuery = this.activatedRoute.snapshot.queryParams['quarter'];
    const teamQuery = this.activatedRoute.snapshot.queryParams['teams'];

    const teamIds = getValueFromQuery(teamQuery);
    const quarterId = getValueFromQuery(quarterQuery)[0];
    this.loadOverview(quarterId, teamIds);
  }

  loadOverview(quarterId: number, teamIds: number[]) {
    this.statistics = this.evaluationService
      .getStatistics(quarterId, teamIds)
      .pipe(catchError(() => {
        return EMPTY;
      }));
  }

  krObjectiveRelation(s: Statistics): number {
    return s.keyResultAmount / s.objectiveAmount || 0;
  }

  krRelation(metrics: number, ordinals: number): { metric: number;
    ordinal: number; } {
    const all = metrics + ordinals;
    if (all === 0) {
      return { metric: 0,
        ordinal: 0 };
    }
    return {
      metric: metrics / all,
      ordinal: ordinals / all
    };
  }

  krProgressRelation(s: Statistics): { fail: number;
    commit: number;
    target: number;
    stretch: number; } {
    const all = s.keyResultsInFailAmount + s.keyResultsInCommitAmount + s.keyResultsInTargetAmount + s.keyResultsInStretchAmount;
    if (all === 0) {
      return { fail: 0,
        commit: 0,
        target: 0,
        stretch: 0 };
    }
    return {
      fail: s.keyResultsInFailAmount / all,
      commit: s.keyResultsInCommitAmount / all,
      target: s.keyResultsInTargetAmount / all,
      stretch: s.keyResultsInStretchAmount / all
    };
  }
}
