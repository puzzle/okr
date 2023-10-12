import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { Observable, ReplaySubject, takeUntil } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OverviewComponent implements OnInit, OnDestroy {
  overviewEntities!: Observable<OverviewEntity[]>;
  private destroyed$: ReplaySubject<boolean> = new ReplaySubject(1);

  constructor(
    private overviewService: OverviewService,
    private refreshDataService: RefreshDataService,
  ) {
    refreshDataService.reloadOverviewSubject.pipe(takeUntil(this.destroyed$)).subscribe(() => {
      this.loadOverview();
    });
  }

  ngOnInit(): void {
    this.overviewEntities = this.overviewService.getOverview();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }

  loadOverview() {
    this.overviewEntities = this.overviewService.getOverview();
  }
}
