import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RefreshDataService {
  public reloadOverviewSubject: Subject<void> = new Subject();

  markDataRefresh() {
    this.reloadOverviewSubject.next();
  }
}
