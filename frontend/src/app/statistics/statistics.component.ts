import { ChangeDetectorRef, Component } from '@angular/core';
import { catchError, combineLatest, EMPTY, Observable, Subject, take } from 'rxjs';
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
  overviewPadding = new Subject<number>();

  statistics = new Observable<Statistics>();

  constructor(
    private refreshDataService: RefreshDataService, private changeDetector: ChangeDetectorRef, private activatedRoute: ActivatedRoute, private evaluationService: EvaluationService
  ) {
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

    this.refreshDataService.okrBannerHeightSubject.subscribe((e) => {
      this.overviewPadding.next(e);
      this.changeDetector.detectChanges();
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
        // this.loadOverview(quarterId, teamIds);
        return EMPTY;
      }));
  }
}
