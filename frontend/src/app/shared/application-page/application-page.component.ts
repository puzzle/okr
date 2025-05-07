import { ChangeDetectorRef, Component, EventEmitter, Input, Output, OnInit, OnDestroy } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable, ReplaySubject, Subject, take, takeUntil } from 'rxjs';
import { RefreshDataService } from '../../services/refresh-data.service';
import { ActivatedRoute } from '@angular/router';
import { ConfigService } from '../../services/config.service';
import { getQueryString, getValueFromQuery, isMobileDevice } from '../common';
import { FilterPageChange } from '../types/model/filter-page-change';

@Component({
  selector: 'app-application-page',
  standalone: false,
  templateUrl: './application-page.component.html',
  styleUrl: './application-page.component.scss'
})
export class ApplicationPageComponent implements OnInit, OnDestroy {
  @Input() elements$?: Observable<any>;

  overviewPadding = new Subject<number>();

  backgroundLogoSrc$ = new BehaviorSubject<string>('assets/images/empty.svg');

  private destroyed$ = new ReplaySubject<boolean>(1);

  @Output() reloadPage = new EventEmitter<FilterPageChange>();


  constructor(
    private refreshDataService: RefreshDataService,
    private activatedRoute: ActivatedRoute,
    private changeDetector: ChangeDetectorRef,
    private configService: ConfigService
  ) {
    this.refreshDataService.reloadOverviewSubject
      .pipe(takeUntil(this.destroyed$))
      .subscribe(() => this.reloadPage.next(this.getFilterPageChange()));

    combineLatest([refreshDataService.teamFilterReady.asObservable(),
      refreshDataService.quarterFilterReady.asObservable()])
      .pipe(take(1))
      .subscribe(() => {
        this.activatedRoute.queryParams.pipe(takeUntil(this.destroyed$))
          .subscribe(() => {
            this.reloadPage.next(this.getFilterPageChange());
          });
      });
  }

  getFilterPageChange(): FilterPageChange {
    const quarterQuery = this.activatedRoute.snapshot.queryParams['quarter'];
    const teamQuery = this.activatedRoute.snapshot.queryParams['teams'];
    const objectiveQuery = this.activatedRoute.snapshot.queryParams['objectiveQuery'];

    const teamIds = getValueFromQuery(teamQuery);
    const quarterId = getValueFromQuery(quarterQuery)[0];
    const objectiveQueryString = getQueryString(objectiveQuery);
    return { quarterId,
      teamIds,
      objectiveQueryString };
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
      }
    });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }
}
