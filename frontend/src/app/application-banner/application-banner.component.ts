import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  HostListener,
  OnDestroy,
  ViewChild,
} from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { DEFAULT_HEADER_HEIGHT_PX, PUZZLE_TOP_BAR_HEIGHT } from '../shared/constantLibary';

@Component({
  selector: 'app-application-banner',
  templateUrl: './application-banner.component.html',
  styleUrls: ['./application-banner.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationBannerComponent implements AfterViewInit, OnDestroy {
  @ViewChild('okrBanner') okrBanner!: ElementRef;
  quarterLabel$: BehaviorSubject<string> = new BehaviorSubject<string>('');
  panelOpenState = false;
  resizeObserver: ResizeObserver;
  bannerHeight: number = DEFAULT_HEADER_HEIGHT_PX;
  lastScrollPosition: number = 0;

  constructor(private refreshDataService: RefreshDataService) {
    this.resizeObserver = new ResizeObserver((entries: ResizeObserverEntry[]) => {
      this.bannerHeight = entries[0].contentRect.height;
      this.changeHeaderAppearance();
    });
  }

  ngAfterViewInit(): void {
    this.resizeObserver.observe(this.okrBanner.nativeElement);
  }

  changeHeaderAppearance() {
    let scrollTop: number = window.scrollY || document.documentElement.scrollTop;
    this.setOKRBannerStyle(scrollTop);
    this.lastScrollPosition = scrollTop;
  }

  setOKRBannerStyle(scrollTop: number) {
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
