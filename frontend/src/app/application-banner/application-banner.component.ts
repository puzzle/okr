import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { PUZZLE_TOP_BAR_HEIGHT } from '../shared/constantLibary';

@Component({
  selector: 'app-application-banner',
  templateUrl: './application-banner.component.html',
  styleUrls: ['./application-banner.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationBannerComponent implements AfterViewInit, OnDestroy {
  lastScrollPosition: number = 0;
  okrBanner: HTMLElement | null = null;
  eventListener: EventListener | null = null;
  quarterLabel$: BehaviorSubject<string> = new BehaviorSubject<string>('');
  panelOpenState = false;
  resizeObserver: ResizeObserver;

  constructor(private refreshDataService: RefreshDataService) {
    this.resizeObserver = new ResizeObserver((entries: ResizeObserverEntry[]) => {
      this.refreshDataService.okrBannerHeightSubject.next(entries[0].contentRect.height);
      this.updateScrollEventListeners(entries[0].contentRect.height);
    });
  }

  ngAfterViewInit(): void {
    this.okrBanner = document.getElementById('okrBanner')!;
    this.resizeObserver.observe(this.okrBanner);
  }

  ngOnDestroy(): void {
    this.removeScrollEventListener();
    this.resizeObserver.disconnect();
  }

  changeHeaderAppearance(bannerHeight: number) {
    let scrollTop: number = window.scrollY || document.documentElement.scrollTop;
    this.setOKRBannerStyle(bannerHeight, scrollTop);
    this.lastScrollPosition = scrollTop;
  }

  setOKRBannerStyle(bannerHeight: number, scrollTop: number) {
    this.okrBanner!.style.top = this.showOrHideBanner(scrollTop, bannerHeight);
    console.log(this.showOrHideBanner(scrollTop, bannerHeight));
  }

  showOrHideBanner(scrollTop: number, bannerHeight: number) {
    return scrollTop > this.lastScrollPosition
      ? '-' + (PUZZLE_TOP_BAR_HEIGHT + bannerHeight) + 'px'
      : PUZZLE_TOP_BAR_HEIGHT + 'px';
  }

  updateScrollEventListeners(bannerHeight: number) {
    this.removeScrollEventListener();
    this.eventListener = () => this.changeHeaderAppearance(bannerHeight);
    window.addEventListener('scroll', this.eventListener);
  }

  removeScrollEventListener() {
    window.removeEventListener('scroll', this.eventListener!);
  }
}
