import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-application-top-bar',
  templateUrl: './application-top-bar.component.html',
  styleUrls: ['./application-top-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationHeaderComponent implements AfterViewInit, OnDestroy {
  lastScrollPosition: number = 0;
  PUZZLE_TOP_BAR_HEIGHT: number = 48;
  okrBanner: HTMLElement | null = null;
  eventListener: EventListener | null = null;

  resizeObserver: ResizeObserver = new ResizeObserver((entries: ResizeObserverEntry[]) => {
    this.updateScrollEventListeners(entries[0].contentRect.height);
  });

  ngAfterViewInit(): void {
    //Define puzzleBanner reference and add resizeObserver
    this.okrBanner = document.getElementById('okrBanner')!;
    this.resizeObserver.observe(this.okrBanner);
  }

  ngOnDestroy(): void {
    this.removeScrollEventListener();
    this.resizeObserver.disconnect();
  }

  changeHeaderAppearance(bannerHeight: number) {
    //Move Banner to negative Top if scrolled down, move to visible top if scrolled up
    let scrollTop: number = window.scrollY || document.documentElement.scrollTop;
    this.setOKRBannerStyle(bannerHeight, scrollTop);
    this.lastScrollPosition = scrollTop;
  }

  setOKRBannerStyle(bannerHeight: number, scrollTop: number) {
    if (scrollTop > bannerHeight) {
      this.okrBanner!.style.top =
        scrollTop > this.lastScrollPosition
          ? '-' + (this.PUZZLE_TOP_BAR_HEIGHT + bannerHeight) + 'px'
          : this.PUZZLE_TOP_BAR_HEIGHT + 'px';
      setTimeout(() => {
        this.okrBanner!.style.position = 'sticky';
      }, 150);
    } else if (scrollTop < 10) {
      this.okrBanner!.style.top = '0px';
      setTimeout(() => {
        this.okrBanner!.style.position = 'relative';
      }, 500);
    }
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
