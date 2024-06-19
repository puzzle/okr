import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { OverviewEntity } from '../../shared/types/model/OverviewEntity';
import {
  BehaviorSubject,
  catchError,
  combineLatest,
  EMPTY,
  ReplaySubject,
  Subject,
  Subscription,
  take,
  takeUntil,
} from 'rxjs';
import { OverviewService } from '../../services/overview.service';
import { ActivatedRoute } from '@angular/router';
import { getQueryString, getValueFromQuery, isMobileDevice, trackByFn } from '../../shared/common';
import { ConfigService } from '../../services/config.service';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { AlignmentService } from '../shared/services/alignment.service';
import { AlignmentLists } from '../shared/types/model/AlignmentLists';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OverviewComponent implements OnInit, OnDestroy {
  overviewEntities$: Subject<OverviewEntity[]> = new Subject<OverviewEntity[]>();
  alignmentLists$: Subject<AlignmentLists> = new Subject<AlignmentLists>();
  protected readonly trackByFn = trackByFn;
  private destroyed$: ReplaySubject<boolean> = new ReplaySubject(1);
  overviewPadding: Subject<number> = new Subject();
  isOverview: boolean = true;

  backgroundLogoSrc$ = new BehaviorSubject<String>('assets/images/empty.svg');

  constructor(
    private overviewService: OverviewService,
    private alignmentService: AlignmentService,
    private refreshDataService: RefreshDataService,
    private activatedRoute: ActivatedRoute,
    private changeDetector: ChangeDetectorRef,
    private configService: ConfigService,
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

  ngOnInit(): void {
    this.refreshDataService.okrBannerHeightSubject.subscribe((e) => {
      this.overviewPadding.next(e);
      this.changeDetector.detectChanges();
    });
    if (!isMobileDevice()) {
      document.getElementById('overview')?.classList.add('bottom-shadow-space');
    }
    this.configService.config$.subscribe({
      next: (config) => {
        if (config.triangles) {
          this.backgroundLogoSrc$.next(config.backgroundLogo);
        }
      },
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

  loadOverview(quarterId?: number, teamIds?: number[], objectiveQuery?: string): void {
    if (this.isOverview) {
      this.loadOverviewData(quarterId, teamIds, objectiveQuery);
    } else {
      this.loadAlignmentData(quarterId, teamIds, objectiveQuery);
    }
  }

  loadOverviewData(quarterId?: number, teamIds?: number[], objectiveQuery?: string): void {
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
      });
  }

  loadAlignmentData(quarterId?: number, teamIds?: number[], objectiveQuery?: string): void {
    this.alignmentService
      .getAlignmentByFilter(quarterId, teamIds, objectiveQuery)
      .pipe(
        catchError(() => {
          this.loadOverview();
          return EMPTY;
        }),
      )
      .subscribe((alignmentLists: AlignmentLists) => {
        this.alignmentLists$.next(alignmentLists);
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }

  switchPage(input: string) {
    if (input == 'diagram' && this.isOverview) {
      this.isOverview = false;
      this.loadOverviewWithParams();
    } else if (input == 'overview' && !this.isOverview) {
      this.isOverview = true;
      this.loadOverviewWithParams();
    }
  }
}
