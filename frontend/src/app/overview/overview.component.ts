import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { Subject } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';
import { NotifierService } from '../shared/services/notifier.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OverviewComponent implements OnInit {
  overviewEntities: Subject<OverviewEntity[]> = new Subject<OverviewEntity[]>();

  constructor(
    private overviewService: OverviewService,
    private notifierService: NotifierService,
  ) {
    this.notifierService.reloadOverview.subscribe(() => {
      this.loadOverview();
    });
  }

  ngOnInit(): void {
    this.loadOverview();
  }

  loadOverview() {
    const urlSearchParam = new URLSearchParams(window.location.search);
    const quarter = urlSearchParam.get('quarter');

    if (quarter) {
      this.overviewService.getOverview(Number(quarter)).subscribe((overviews) => {
        this.overviewEntities.next(overviews);
      });
    } else {
      this.overviewService.getOverview().subscribe((overviews) => {
        this.overviewEntities.next(overviews);
      });
    }
  }
}
