import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  HostListener,
  OnDestroy,
  ViewChild
} from '@angular/core';
import { DEFAULT_HEADER_HEIGHT_PX, PUZZLE_TOP_BAR_HEIGHT } from '../../constant-library';
import { RefreshDataService } from '../../../services/refresh-data.service';

@Component({
  selector: 'app-application-banner',
  templateUrl: './application-banner.component.html',
  styleUrls: ['./application-banner.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class ApplicationBannerComponent implements AfterViewInit, OnDestroy {
  @ViewChild('okrBanner') okrBanner!: ElementRef;

  resizeObserver: ResizeObserver;

  bannerHeight: number = DEFAULT_HEADER_HEIGHT_PX;

  lastScrollPosition = 0;

  constructor(private refreshDataService: RefreshDataService) {
    this.resizeObserver = new ResizeObserver((entries: ResizeObserverEntry[]) => {
      const newBannerHeight = entries[0].contentRect.height;
      if (newBannerHeight != this.bannerHeight) {
        this.bannerHeight = newBannerHeight;
        this.refreshDataService.okrBannerHeightSubject.next(this.bannerHeight);
      }
    });
  }

  ngAfterViewInit(): void {
    this.resizeObserver.observe(this.okrBanner.nativeElement);
    this.changeHeaderAppearance();
  }

  changeHeaderAppearance() {
    const scrollTop: number = window.scrollY || document.documentElement.scrollTop;
    this.refreshBanner(scrollTop);
    this.lastScrollPosition = scrollTop;
  }

  refreshBanner(scrollTop: number) {
    const newBannerPadding = this.getBannerTopPadding(scrollTop);
    this.okrBanner.nativeElement.style.top = newBannerPadding + 'px';

    const overviewPadding = this.getOverviewPadding(newBannerPadding, this.bannerHeight);
    this.refreshDataService.okrBannerHeightSubject.next(overviewPadding);
  }

  getBannerTopPadding(scrollTop: number) {
    return scrollTop > this.lastScrollPosition
      ? 0 - (PUZZLE_TOP_BAR_HEIGHT + this.bannerHeight)
      : PUZZLE_TOP_BAR_HEIGHT;
  }

  getOverviewPadding(newBannerPadding: number, paddingAmount: number): number {
    return newBannerPadding < 0 ? PUZZLE_TOP_BAR_HEIGHT * 2 : paddingAmount;
  }

  @HostListener('window:scroll')
  scroll() {
    this.changeHeaderAppearance();
  }

  ngOnDestroy(): void {
    this.resizeObserver.disconnect();
  }
}
