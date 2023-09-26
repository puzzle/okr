import { AfterViewInit, Component, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-application-banner',
  templateUrl: './application-banner.component.html',
  styleUrls: ['./application-banner.component.scss'],
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
    //Check if further scrolled than bannerHeight
    if (scrollTop > bannerHeight) {
      //Check if scrolled up or down and hide or show banner according to this value
      this.okrBanner!.style.top = this.showOrHideBanner(scrollTop, bannerHeight);
      this.setPositionOfBanner('sticky');
    } else if (scrollTop < 10) {
      /*If User is at the top of the page set top to 0 pixels and make banner relative
       * so it scrolls with the user */
      this.okrBanner!.style.top = '0px';
      this.setPositionOfBanner('relative');
    }
  }

  //Show or hide banner with setting top position to negative or to positive
  showOrHideBanner(scrollTop: number, bannerHeight: number) {
    return scrollTop > this.lastScrollPosition
      ? '-' + (this.PUZZLE_TOP_BAR_HEIGHT + bannerHeight) + 'px'
      : this.PUZZLE_TOP_BAR_HEIGHT + 'px';
  }

  //Set position of banner with timeout so banner doesn't 'float' in
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
