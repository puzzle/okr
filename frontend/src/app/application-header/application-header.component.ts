import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';

@Component({
  selector: 'app-application-header',
  templateUrl: './application-header.component.html',
  styleUrls: ['./application-header.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationHeaderComponent implements OnInit, AfterViewInit, OnDestroy {
  lastScrollPosition: number = 0;
  PUZZLE_TOP_BAR_HEIGHT: number = 48;
  okrBanner: HTMLElement | null = null;
  eventListener: EventListener | null = null;

  resizeObserver: ResizeObserver = new ResizeObserver((entries: ResizeObserverEntry[]) => {
    this.removeScrollEventListener();
    this.setScrollEventListener(entries[0].contentRect.height);
  });

  constructor() {}

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    //Define puzzleBanner reference and add resizeObserver
    this.okrBanner = document.getElementById('okrBanner')!;
    this.resizeObserver.observe(this.okrBanner);
  }

  ngOnDestroy(): void {
    this.removeScrollEventListener();
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
        this.okrBanner!.style.position = 'fixed';
      }, 200);
    } else if (scrollTop == 0) {
      this.okrBanner!.style.position = 'relative';
    }
  }

  setScrollEventListener(bannerHeight: number) {
    this.eventListener = () => this.changeHeaderAppearance(bannerHeight);
    window.addEventListener('scroll', this.eventListener);
  }

  removeScrollEventListener() {
    window.removeEventListener('scroll', this.eventListener!);
  }
}
