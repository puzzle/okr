import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { FilterModel } from '../types/model/FilterModel';

@Injectable({
  providedIn: 'root',
})
export class RefreshDataService {
  private reloadOverviewSubject: Subject<FilterModel> = new Subject();
  filterValues: FilterModel = { objectiveQuery: null, teamIds: null, quarterId: null } as FilterModel;

  public get getReloadOverviewSubject(): Observable<FilterModel> {
    return this.reloadOverviewSubject.asObservable();
  }
  markDataRefresh() {
    if (!Object.values(this.filterValues).some((e) => e === null)) {
      this.reloadOverviewSubject.next(this.filterValues);
    }
  }

  refreshTeamFilter(teamIds: number[]) {
    this.filterValues.teamIds = teamIds;
    this.markDataRefresh();
  }

  refreshObjectiveFilter(query: string) {
    this.filterValues.objectiveQuery = query;
    this.markDataRefresh();
  }

  refreshQuarterFilter(quarterId: number | undefined) {
    this.filterValues.quarterId = quarterId;
    this.markDataRefresh();
  }
}
