import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { catchError, combineLatest, EMPTY, ReplaySubject, Subject, take, takeUntil } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';
import { ActivatedRoute } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { getQueryString, getValueFromQuery, isMobileDevice, trackByFn } from '../shared/common';
import { AlignmentService } from '../shared/services/alignment.service';
import { Dashboard } from '../shared/types/model/Dashboard';

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
  hasAdminAccess: ReplaySubject<boolean> = new ReplaySubject<boolean>(1);
  overviewPadding: Subject<number> = new Subject();
  private service: AlignmentService | OverviewService;
  isDiagram: boolean = true;

  constructor(
    private overviewService: OverviewService,
    private alignmentService: AlignmentService,
    private refreshDataService: RefreshDataService,
    private activatedRoute: ActivatedRoute,
    private changeDetector: ChangeDetectorRef,
  ) {
    this.service = this.isDiagram ? this.alignmentService : this.overviewService;
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

  ngOnInit(): void {
    this.refreshDataService.okrBannerHeightSubject.subscribe((e) => {
      this.overviewPadding.next(e);
      this.changeDetector.detectChanges();
    });
    if (!isMobileDevice()) {
      document.getElementById('overview')?.classList.add('bottom-shadow-space');
    }
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
    this.service
      .getOverview(quarterId, teamIds, objectiveQuery)
      .pipe(
        catchError(() => {
          this.loadOverview();
          return EMPTY;
        }),
      )
      .subscribe((dashboard: Dashboard) => {
        this.hasAdminAccess.next(dashboard.adminAccess);
        this.overviewEntities$.next(dashboard.overviews);
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }
}
