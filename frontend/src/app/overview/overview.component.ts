import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { catchError, EMPTY, ReplaySubject, Subject, takeUntil } from 'rxjs';
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
export class OverviewComponent implements OnInit, OnDestroy {
  overviewEntities$: Subject<OverviewEntity[]> = new Subject<OverviewEntity[]>();
  protected readonly trackByFn = trackByFn;
  private destroyed$: ReplaySubject<boolean> = new ReplaySubject(1);

  constructor(
    private overviewService: OverviewService,
    private refreshDataService: RefreshDataService,
    private activatedRoute: ActivatedRoute,
  ) {
    this.refreshDataService.reloadOverviewSubject
      .pipe(takeUntil(this.destroyed$))
      .subscribe(() => this.loadOverviewWithParams());

    this.activatedRoute.queryParams.pipe(takeUntil(this.destroyed$)).subscribe(() => this.loadOverviewWithParams());
  }

  ngOnInit(): void {
    this.loadOverview();
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
      .subscribe((overviews) => this.overviewEntities$.next(overviews));
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }
}
