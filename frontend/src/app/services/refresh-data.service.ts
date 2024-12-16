import { Injectable } from "@angular/core";
import { BehaviorSubject, Subject } from "rxjs";
import { DEFAULT_HEADER_HEIGHT_PX } from "../shared/constantLibary";

@Injectable({
  providedIn: "root"
})
export class RefreshDataService {
  public reloadOverviewSubject = new Subject<void>();

  public reloadKeyResultSubject = new Subject<void>();

  public quarterFilterReady: Subject<void> = new Subject<void>();

  public teamFilterReady: Subject<void> = new Subject<void>();

  public okrBannerHeightSubject: BehaviorSubject<number> = new BehaviorSubject<number>(DEFAULT_HEADER_HEIGHT_PX);

  markDataRefresh () {
    this.reloadOverviewSubject.next();
  }
}
