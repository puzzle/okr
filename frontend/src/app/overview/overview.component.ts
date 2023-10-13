import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { catchError, EMPTY, Subject } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';
import { NotifierService } from '../shared/services/notifier.service';
import { ActivatedRoute } from '@angular/router';

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
    private activatedRoute: ActivatedRoute,
  ) {
    this.notifierService.reloadOverview.subscribe(() => {
      this.loadOverview();
    });
  }

  ngOnInit(): void {
    this.loadOverview();
  }

  loadOverview() {
    const quarterId = this.activatedRoute.snapshot.queryParams['quarter'];
    if (quarterId !== undefined) {
      this.overviewService
        .getOverview(+quarterId)
        .pipe(
          catchError(() => {
            this.loadDefaultOverview();
            return EMPTY;
          }),
        )
        .subscribe((overviews) => {
          this.overviewEntities.next(overviews);
        });
    } else {
      this.loadDefaultOverview();
    }
  }

  loadDefaultOverview() {
    this.overviewService.getOverview().subscribe((overviews) => {
      this.overviewEntities.next(overviews);
    });
  }
}
