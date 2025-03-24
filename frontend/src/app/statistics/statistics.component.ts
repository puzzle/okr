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
    return {
      metric: metrics / all || 0,
      ordinal: ordinals / all || 0
    };
  }

  krProgressRelation(s: Statistics): { fail: number;
    commit: number;
    target: number;
    stretch: number;
    multiplier: number; } {
    const all = s.keyResultsInFailAmount + s.keyResultsInCommitAmount + s.keyResultsInTargetAmount + s.keyResultsInStretchAmount;

    const r =
        {
          fail: s.keyResultsInFailAmount / all || 0,
          commit: s.keyResultsInCommitAmount / all || 0,
          target: s.keyResultsInTargetAmount / all || 0,
          stretch: s.keyResultsInStretchAmount / all || 0
        };
    const max = Math.max(
      r.fail, r.commit, r.target, r.stretch
    );
    const multiplier = 1 / max;
    return { ...r,
      multiplier: multiplier };
  }
}
