import { ChangeDetectorRef, Component, EventEmitter, Input, Output, OnInit, OnDestroy, inject } from '@angular/core';
import { BehaviorSubject, debounceTime, Observable, ReplaySubject, Subject, takeUntil } from 'rxjs';
import { RefreshDataService } from '../../services/refresh-data.service';
import { ActivatedRoute, Params } from '@angular/router';
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
  private refreshDataService = inject(RefreshDataService);

  private activatedRoute = inject(ActivatedRoute);

  private changeDetector = inject(ChangeDetectorRef);

  private configService = inject(ConfigService);

  @Input() elements$?: Observable<any>;

  overviewPadding = new Subject<number>();

  backgroundLogoSrc$ = new BehaviorSubject<string>('assets/images/empty.svg');

  hasSelectedTeams = false;

  private destroyed$ = new ReplaySubject<boolean>(1);

  @Output() reloadPage = new EventEmitter<FilterPageChange>();

  constructor() {
    this.refreshDataService.reloadOverviewSubject
      .pipe(takeUntil(this.destroyed$))
      .subscribe(() => this.reloadPage.next(this.getFilterPageChange(this.activatedRoute.snapshot.queryParams)));

    this.activatedRoute.queryParams
      .pipe(takeUntil(this.destroyed$), debounceTime(10))
      .subscribe((params: Params) => {
        const teamIds = getValueFromQuery(params['teams']);
        this.hasSelectedTeams = teamIds && teamIds.length > 0;

        if (params['quarter'] !== undefined) {
          this.reloadPage.next(this.getFilterPageChange(params));
        }
      });
  }

  getFilterPageChange(params: Params): FilterPageChange {
    const quarterQuery = params['quarter'];
    const teamQuery = params['teams'];
    const objectiveQuery = params['objectiveQuery'];

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
