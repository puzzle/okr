import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { catchError, EMPTY, ReplaySubject, Subject, takeUntil } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';
import { ActivatedRoute } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OverviewComponent implements OnInit, OnDestroy {
  overviewEntities$: Subject<OverviewEntity[]> = new Subject<OverviewEntity[]>();
  private destroyed$: ReplaySubject<boolean> = new ReplaySubject(1);

  constructor(
    private overviewService: OverviewService,
    private refreshDataService: RefreshDataService,
    private activatedRoute: ActivatedRoute,
  ) {
    this.refreshDataService.reloadOverviewSubject.pipe(takeUntil(this.destroyed$)).subscribe(() => this.loadOverview());
  }

  ngOnInit(): void {
    this.loadOverview();
  }

  loadOverview() {
    const quarterId = this.activatedRoute.snapshot.queryParams['quarter'];
    const teamQuery = this.activatedRoute.snapshot.queryParams['teams'];

    const teamIds = Array.from([teamQuery])
      .map((id) => Number(id))
      .filter((id) => Number.isInteger(id));

    this.getOverview(quarterId, teamIds);
  }

  getOverview(quarterId?: number, teamIds?: number[]) {
    this.overviewService
      .getOverview(quarterId, teamIds)
      .pipe(
        catchError(() => {
          this.getOverview();
          return EMPTY;
        }),
      )
      .subscribe((overviews) => {
        this.overviewEntities$.next(overviews);
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }
}
