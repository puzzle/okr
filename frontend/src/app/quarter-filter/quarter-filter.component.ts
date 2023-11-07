import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { QuarterService } from '../shared/services/quarter.service';
import { Quarter } from '../shared/types/model/Quarter';
import { Subject } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { getValueFromQuery } from '../shared/common';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-quarter-filter',
  templateUrl: './quarter-filter.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class QuarterFilterComponent implements OnInit {
  quarters: Subject<Quarter[]> = new Subject<Quarter[]>();
  quarterId: number = -1;

  constructor(
    private quarterService: QuarterService,
    private router: Router,
    private route: ActivatedRoute,
    private refreshDataService: RefreshDataService,
  ) {}

  ngOnInit() {
    this.quarterService.getAllQuarters().subscribe((quarters) => {
      this.quarters.next(quarters);
      const quarterQuery = this.route.snapshot.queryParams['quarter'];
      const quarterId: number = getValueFromQuery(quarterQuery)[0];
      if (quarters.map((quarter) => quarter.id).includes(quarterId)) {
        this.quarterId = quarterId;
        this.changeDisplayedQuarter();
      } else {
        if (quarterQuery !== undefined) {
          this.quarterId = quarters[0].id;
          this.changeDisplayedQuarter();
        } else {
          this.quarterId = quarters[0].id;
          this.refreshDataService.quarterFilterReady.next();
        }
      }
    });
  }

  changeDisplayedQuarter() {
    const id = this.quarterId;
    this.router
      .navigate([], { queryParams: { quarter: id } })
      .then(() => this.refreshDataService.quarterFilterReady.next());
  }
}
