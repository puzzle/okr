import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { Observable } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';
import { NotifierService } from '../shared/services/notifier.service';

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
    private notifierService: NotifierService,
  ) {
    this.notifierService.reloadOverview.subscribe(() => {
      this.overviewEntities = this.overviewService.getOverview();
    });
  }

  ngOnInit(): void {
    this.overviewEntities = this.overviewService.getOverview();
  }
}
