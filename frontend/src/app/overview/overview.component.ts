import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { ReplaySubject, takeUntil } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OverviewComponent implements OnInit, OnDestroy {
  overviewEntities$: ReplaySubject<OverviewEntity[]> = new ReplaySubject<OverviewEntity[]>(1);
  private destroyed$: ReplaySubject<boolean> = new ReplaySubject(1);

  constructor(
    private overviewService: OverviewService,
    private refreshDataService: RefreshDataService,
  ) {
    this.refreshDataService.reloadOverviewSubject.pipe(takeUntil(this.destroyed$)).subscribe(() => this.loadOverview());
  }

  ngOnInit(): void {
    this.loadOverview();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }

  loadOverview() {
    this.overviewService.getOverview().subscribe((value) => {
      this.overviewEntities$.next(value);
    });
  }
}
