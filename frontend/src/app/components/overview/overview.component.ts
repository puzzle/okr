import { ChangeDetectionStrategy, Component } from '@angular/core';
import { OverviewEntity } from '../../shared/types/model/overview-entity';
import { catchError, EMPTY, Subject } from 'rxjs';
import { OverviewService } from '../../services/overview.service';
import { trackByFn } from '../../shared/common';
import { FilterPageChange } from '../../shared/types/model/filter-page-change';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class OverviewComponent {
  overviewEntities$: Subject<OverviewEntity[]> = new Subject<OverviewEntity[]>();

  protected readonly trackByFn = trackByFn;

  constructor(private overviewService: OverviewService) {}

  loadOverview(filter: FilterPageChange) {
    this.overviewService
      .getOverview(filter.quarterId, filter.teamIds, filter.objectiveQueryString)
      .pipe(catchError(() => {
        this.loadOverview({} as FilterPageChange);
        return EMPTY;
      }))
      .subscribe((overviews) => {
        this.overviewEntities$.next(overviews);
      });
  }
}
