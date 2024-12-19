import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { OverviewEntity } from '../../shared/types/model/OverviewEntity';
import {
  BehaviorSubject,
  catchError,
  combineLatest,
  EMPTY,
  ReplaySubject,
  Subject,
  take,
  takeUntil
} from 'rxjs';
import { OverviewService } from '../../services/overview.service';
import { ActivatedRoute } from '@angular/router';
import { RefreshDataService } from '../../services/refresh-data.service';
import { getQueryString, getValueFromQuery, isMobileDevice, trackByFn } from '../../shared/common';
import { ConfigService } from '../../services/config.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OverviewComponent implements OnInit, OnDestroy {
  overviewEntities$: Subject<OverviewEntity[]> = new Subject<OverviewEntity[]>();

  protected readonly trackByFn = trackByFn;

  private destroyed$ = new ReplaySubject<boolean>(1);

  overviewPadding = new Subject<number>();

  backgroundLogoSrc$ = new BehaviorSubject<string>('assets/images/empty.svg');

  constructor (
    private overviewService: OverviewService,
    private refreshDataService: RefreshDataService,
    private activatedRoute: ActivatedRoute,
    private changeDetector: ChangeDetectorRef,
    private configService: ConfigService
  ) {
    this.refreshDataService.reloadOverviewSubject
      .pipe(takeUntil(this.destroyed$))
      .subscribe(() => this.loadOverviewWithParams());

    combineLatest([refreshDataService.teamFilterReady.asObservable(),
      refreshDataService.quarterFilterReady.asObservable()])
      .pipe(take(1))
      .subscribe(() => {
        this.activatedRoute.queryParams.pipe(takeUntil(this.destroyed$))
          .subscribe(() => {
            this.loadOverviewWithParams();
          });
      });
  }

  ngOnInit (): void {
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
      }
    });
  }

  loadOverviewWithParams () {
    const quarterQuery = this.activatedRoute.snapshot.queryParams['quarter'];
    const teamQuery = this.activatedRoute.snapshot.queryParams['teams'];
    const objectiveQuery = this.activatedRoute.snapshot.queryParams['objectiveQuery'];

    const teamIds = getValueFromQuery(teamQuery);
    const quarterId = getValueFromQuery(quarterQuery)[0];
    const objectiveQueryString = getQueryString(objectiveQuery);
    this.loadOverview(quarterId, teamIds, objectiveQueryString);
  }

  loadOverview (quarterId?: number, teamIds?: number[], objectiveQuery?: string) {
    this.overviewService
      .getOverview(quarterId, teamIds, objectiveQuery)
      .pipe(catchError(() => {
        this.loadOverview();
        return EMPTY;
      }))
      .subscribe((overviews) => {
        this.overviewEntities$.next(overviews);
      });
  }

  ngOnDestroy (): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }
}
