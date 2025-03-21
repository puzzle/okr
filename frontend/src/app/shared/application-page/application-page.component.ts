import { ChangeDetectorRef, Component, EventEmitter, Input, Output, OnInit, OnDestroy } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable, ReplaySubject, Subject, take, takeUntil } from 'rxjs';
import { RefreshDataService } from '../../services/refresh-data.service';
import { ActivatedRoute } from '@angular/router';
import { ConfigService } from '../../services/config.service';
import { isMobileDevice } from '../common';

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

  @Output() reloadPage = new EventEmitter();


  constructor(
    private refreshDataService: RefreshDataService,
    private activatedRoute: ActivatedRoute,
    private changeDetector: ChangeDetectorRef,
    private configService: ConfigService
  ) {
    this.refreshDataService.reloadOverviewSubject
      .pipe(takeUntil(this.destroyed$))
      .subscribe(() => this.reloadPage.next(true));

    combineLatest([refreshDataService.teamFilterReady.asObservable(),
      refreshDataService.quarterFilterReady.asObservable()])
      .pipe(take(1))
      .subscribe(() => {
        this.activatedRoute.queryParams.pipe(takeUntil(this.destroyed$))
          .subscribe(() => {
            this.reloadPage.next(true);
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
      }
    });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }
}
