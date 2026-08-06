import { ChangeDetectorRef, Component, OnInit, OnDestroy, inject, input } from '@angular/core';
import { BehaviorSubject, ReplaySubject, Subject, takeUntil } from 'rxjs';
import { ConfigService } from '../../services/config.service';
import { RefreshDataService } from '../../services/refresh-data.service';
import { isMobileDevice } from '../common';

@Component({
  selector: 'app-application-page',
  standalone: false,
  templateUrl: './application-page.component.html',
  styleUrl: './application-page.component.scss'
})
export class ApplicationPageComponent implements OnInit, OnDestroy {
  private refreshDataService = inject(RefreshDataService);

  private changeDetector = inject(ChangeDetectorRef);

  private configService = inject(ConfigService);

  overviewPadding = new Subject<number>();

  backgroundLogoSrc$ = new BehaviorSubject<string>('assets/images/empty.svg');

  isEmpty = input<boolean>();

  private destroyed$ = new ReplaySubject<boolean>(1);

  ngOnInit(): void {
    this.refreshDataService.okrBannerHeightSubject
      .pipe(takeUntil(this.destroyed$))
      .subscribe((e) => {
        this.overviewPadding.next(e);
        this.changeDetector.detectChanges();
      });

    if (!isMobileDevice()) {
      document.getElementById('overview')?.classList.add('bottom-shadow-space');
    }

    this.configService.config$
      .pipe(takeUntil(this.destroyed$))
      .subscribe({
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
