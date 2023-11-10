import { ChangeDetectionStrategy, Component, OnDestroy } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { catchError, combineLatest, EMPTY, Observable, ReplaySubject, Subject, take, takeUntil } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';
import { ActivatedRoute } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { getQueryString, getValueFromQuery, trackByFn } from '../shared/common';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OverviewComponent implements OnDestroy {
  overviewEntities$: Subject<OverviewEntity[]> = new Subject<OverviewEntity[]>();
  protected readonly trackByFn = trackByFn;
  private destroyed$: ReplaySubject<boolean> = new ReplaySubject(1);
  hasWriteAllAccess: boolean = false;

  constructor(
    private overviewService: OverviewService,
    private refreshDataService: RefreshDataService,
    private activatedRoute: ActivatedRoute,
  ) {
    this.refreshDataService.reloadOverviewSubject
      .pipe(takeUntil(this.destroyed$))
      .subscribe(() => this.loadOverviewWithParams());

    combineLatest([
      refreshDataService.teamFilterReady.asObservable(),
      refreshDataService.quarterFilterReady.asObservable(),
    ])
      .pipe(take(1))
      .subscribe(() => {
        this.activatedRoute.queryParams.pipe(takeUntil(this.destroyed$)).subscribe(() => {
          this.loadOverviewWithParams();
        });
      });
  }

  loadOverviewWithParams() {
    const quarterQuery = this.activatedRoute.snapshot.queryParams['quarter'];
    const teamQuery = this.activatedRoute.snapshot.queryParams['teams'];
    const objectiveQuery = this.activatedRoute.snapshot.queryParams['objectiveQuery'];

    const teamIds = getValueFromQuery(teamQuery);
    const quarterId = getValueFromQuery(quarterQuery)[0];
    const objectiveQueryString = getQueryString(objectiveQuery);
    this.loadOverview(quarterId, teamIds, objectiveQueryString);
  }

  loadOverview(quarterId?: number, teamIds?: number[], objectiveQuery?: string) {
    this.overviewService
      .getOverview(quarterId, teamIds, objectiveQuery)
      .pipe(
        catchError(() => {
          this.loadOverview();
          return EMPTY;
        }),
      )
      .subscribe((overviews) => {
        this.overviewEntities$.next(overviews);
        this.hasWriteAllAccess = overviews.filter((overview) => overview.writable!).length > 0;
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }
}
