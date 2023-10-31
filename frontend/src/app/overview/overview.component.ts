import { ChangeDetectionStrategy, Component, OnDestroy } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { catchError, EMPTY, ReplaySubject, Subject, takeUntil, tap } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { trackByFn } from '../shared/common';
import { FilterModel } from '../shared/types/model/FilterModel';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OverviewComponent implements OnDestroy {
  overviewEntities$: Subject<OverviewEntity[]> = new Subject<OverviewEntity[]>();
  protected readonly trackByFn = trackByFn;
  private destroyed$: ReplaySubject<boolean> = new ReplaySubject(1);

  constructor(
    private overviewService: OverviewService,
    private refreshDataService: RefreshDataService,
  ) {
    this.refreshDataService.getReloadOverviewSubject
      .pipe(takeUntil(this.destroyed$))
      .pipe(tap(console.log))
      .subscribe((filterValue: FilterModel) => this.loadOverview(filterValue));
  }

  loadOverview(filterValue?: FilterModel) {
    this.overviewService
      .getOverview(filterValue)
      .pipe(
        catchError(() => {
          this.loadOverview();
          return EMPTY;
        }),
      )
      .subscribe((overviews) => this.overviewEntities$.next(overviews));
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }
}
