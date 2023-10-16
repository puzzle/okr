import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { QuarterService } from '../shared/services/quarter.service';
import { Quarter } from '../shared/types/model/Quarter';
import { Subject } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-quarter-filter',
  templateUrl: './quarter-filter.component.html',
  styleUrls: ['./quarter-filter.component.scss'],
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
      const quarterId = this.route.snapshot.queryParams['quarter'];
      if (quarterId !== undefined && quarters.map((quarter) => quarter.id).includes(+quarterId)) {
        this.quarterId = +quarterId;
      } else {
        this.quarterId = quarters[0].id;
        if (this.router.url !== '/') {
          this.changeDisplayedQuarter();
        }
      }
    });
  }

  changeDisplayedQuarter() {
    const id = this.quarterId;
    this.router.navigate([], { queryParams: { quarter: id } }).then(() => {
      this.refreshDataService.markDataRefresh();
    });
  }
}
