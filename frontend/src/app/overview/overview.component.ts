import { ChangeDetectionStrategy, Component, effect, OnInit } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { Observable } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OverviewComponent implements OnInit {
  overviewEntities!: Observable<OverviewEntity[]>;

  constructor(
    private overviewService: OverviewService,
    private refreshDataService: RefreshDataService,
  ) {
    console.log('OverviewComponent.constructor');
  }

  ngOnInit(): void {
    this.loadKeyResult();

    effect(() => {
      console.log(`The current count is: ${this.refreshDataService.dataRefresh()}`);
      this.loadKeyResult();
    });
  }

  loadKeyResult() {
    this.refreshDataService.dataRefresh.set(Date.now());
    this.overviewEntities = this.overviewService.getOverview();
  }
}
