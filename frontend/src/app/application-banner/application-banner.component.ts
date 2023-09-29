import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy } from '@angular/core';
import { timeout } from 'rxjs';

@Component({
  selector: 'app-application-banner',
  templateUrl: './application-banner.component.html',
  styleUrls: ['./application-banner.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationBannerComponent implements AfterViewInit, OnDestroy {
  lastScrollPosition: number = 0;
  PUZZLE_TOP_BAR_HEIGHT: number = 48;
  okrBanner: HTMLElement | null = null;
  eventListener: EventListener | null = null;

  resizeObserver: ResizeObserver = new ResizeObserver((entries: ResizeObserverEntry[]) => {
    this.updateScrollEventListeners(entries[0].contentRect.height);
  });

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
  }

  showOrHideBanner(scrollTop: number, bannerHeight: number) {
    return scrollTop > this.lastScrollPosition
      ? '-' + (this.PUZZLE_TOP_BAR_HEIGHT + bannerHeight) + 'px'
      : this.PUZZLE_TOP_BAR_HEIGHT + 'px';
  }

  setPositionOfBanner(position: string) {
    setTimeout(() => {
      this.okrBanner!.style.position = position;
    }, 500);
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
