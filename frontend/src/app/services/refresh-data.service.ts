import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { DEFAULT_HEADER_HEIGHT_PX } from '../shared/constantLibary';

@Injectable({
  providedIn: 'root',
})
export class RefreshDataService {
  public reloadOverviewSubject: Subject<void> = new Subject();
  public reloadKeyResultSubject: Subject<void> = new Subject();
  public reloadAlignmentSubject: Subject<boolean | null | undefined> = new Subject();

  public quarterFilterReady: Subject<void> = new Subject<void>();
  public teamFilterReady: Subject<void> = new Subject<void>();

  public okrBannerHeightSubject: BehaviorSubject<number> = new BehaviorSubject<number>(DEFAULT_HEADER_HEIGHT_PX);

  markDataRefresh(reload?: boolean | null) {
    this.reloadOverviewSubject.next();
    this.reloadAlignmentSubject.next(reload);
  }
}
