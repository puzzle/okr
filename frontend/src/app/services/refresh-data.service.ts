import { Injectable } from '@angular/core';
import { BehaviorSubject, ReplaySubject, Subject } from 'rxjs';
import { DEFAULT_HEADER_HEIGHT_PX } from '../shared/constant-library';

@Injectable({
  providedIn: 'root'
})
export class RefreshDataService {
  public reloadOverviewSubject = new Subject<void>();

  public reloadKeyResultSubject = new Subject<void>();

  public quarterFilterReady = new ReplaySubject<void>(1);

  public teamFilterReady = new ReplaySubject<void>(1);

  public okrBannerHeightSubject: BehaviorSubject<number> = new BehaviorSubject<number>(DEFAULT_HEADER_HEIGHT_PX);

  markDataRefresh() {
    this.reloadOverviewSubject.next();
  }
}
