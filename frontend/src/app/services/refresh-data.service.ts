import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { DEFAULT_HEADER_HEIGHT_PX } from '../shared/constant-library';
import { OverviewService } from './overview.service';
import { StatisticsService } from './statistics.service';

@Injectable({
  providedIn: 'root'
})
export class RefreshDataService {
  overviewService = inject(OverviewService);

  statisticsService = inject(StatisticsService);

  public reloadOverviewSubject = new Subject<void>();

  public reloadKeyResultSubject = new Subject<void>();

  public okrBannerHeightSubject: BehaviorSubject<number> = new BehaviorSubject<number>(DEFAULT_HEADER_HEIGHT_PX);

  markDataRefresh() {
    this.reloadOverviewSubject.next();
    this.overviewService.reload();
    this.statisticsService.reload();
  }
}
