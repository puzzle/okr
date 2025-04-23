import { Component } from '@angular/core';
import { catchError, EMPTY, Observable } from 'rxjs';
import { EvaluationService } from '../services/evaluation.service';
import { Statistics } from '../shared/types/model/statistics';
import { FilterPageChange } from '../shared/types/model/filter-page-change';

@Component({
  selector: 'app-statistics',
  standalone: false,
  templateUrl: './statistics.component.html',
  styleUrl: './statistics.component.scss'
})
export class StatisticsComponent {
  statistics = new Observable<Statistics>();

  constructor(private evaluationService: EvaluationService) {
  }

  loadOverview(filterpage: FilterPageChange) {
    this.statistics = this.evaluationService
      .getStatistics(filterpage.quarterId, filterpage.teamIds)
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
