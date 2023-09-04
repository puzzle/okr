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
  puzzleBanner: HTMLElement | null = null;
  eventListener: EventListener | null = null;

  resizeObserver: ResizeObserver = new ResizeObserver((entries: ResizeObserverEntry[]) => {
    this.removeScrollEventListener(this.puzzleBanner!, this.puzzleBanner!.offsetHeight);
    this.setScrollEventListener(this.puzzleBanner!, entries[0].contentRect.height);
  });

  constructor() {}

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    //Define puzzleBanner reference and add resizeObserver
    this.puzzleBanner = document.getElementById('okrBanner')!;
    this.resizeObserver.observe(this.puzzleBanner);
  }

  ngOnDestroy(): void {
    this.removeScrollEventListener(this.puzzleBanner!, this.puzzleBanner!.offsetHeight);
  }

  changeHeaderAppearance(banner: HTMLElement, bannerHeight: number) {
    //Move Banner to negative Top if scrolled down, move to visible top if scrolled up
    let scrollTop: number = window.scrollY || document.documentElement.scrollTop;
    banner!.style.top =
      scrollTop > this.lastScrollPosition
        ? '-' + (bannerHeight + this.PUZZLE_TOP_BAR_HEIGHT) + 'px'
        : this.PUZZLE_TOP_BAR_HEIGHT + 'px';
    this.lastScrollPosition = scrollTop;
  }

  setScrollEventListener(banner: HTMLElement, bannerHeight: number) {
    this.eventListener = () => this.changeHeaderAppearance(banner, bannerHeight);
    window.addEventListener('scroll', this.eventListener);
  }

  removeScrollEventListener(banner: HTMLElement, bannerHeight: number) {
    window.removeEventListener('scroll', this.eventListener!);
  }
}
