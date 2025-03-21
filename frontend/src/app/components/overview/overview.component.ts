import { ChangeDetectionStrategy, Component } from '@angular/core';
import { OverviewEntity } from '../../shared/types/model/overview-entity';
import { catchError, EMPTY, Subject } from 'rxjs';
import { OverviewService } from '../../services/overview.service';
import { ActivatedRoute } from '@angular/router';
import { getQueryString, getValueFromQuery, trackByFn } from '../../shared/common';

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

  constructor(private overviewService: OverviewService,
    private activatedRoute: ActivatedRoute) {
  }

  loadOverviewWithParams() {
    const quarterQuery = this.activatedRoute.snapshot.queryParams['quarter'];
    const teamQuery = this.activatedRoute.snapshot.queryParams['teams'];
    const objectiveQuery = this.activatedRoute.snapshot.queryParams['objectiveQuery'];

    const teamIds = getValueFromQuery(teamQuery);
    const quarterId = getValueFromQuery(quarterQuery)[0];
    const objectiveQueryString = getQueryString(objectiveQuery);
    this.loadOverview(quarterId, teamIds, objectiveQueryString);
  }

  loadOverview(quarterId?: number, teamIds?: number[], objectiveQuery?: string) {
    this.overviewService
      .getOverview(quarterId, teamIds, objectiveQuery)
      .pipe(catchError(() => {
        this.loadOverview();
        return EMPTY;
      }))
      .subscribe((overviews) => {
        this.overviewEntities$.next(overviews);
      });
  }
}
