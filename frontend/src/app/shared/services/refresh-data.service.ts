import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RefreshDataService {
  public reloadOverviewSubject: Subject<void> = new Subject();

  public quarterFilterReady: Subject<void> = new Subject<void>();
  public teamFilterReady: Subject<void> = new Subject<void>();

  markDataRefresh() {
    this.reloadOverviewSubject.next();
  }
}
