import { ChangeDetectionStrategy, Component, EventEmitter, OnInit, Output } from '@angular/core';
import { QuarterService } from '../shared/services/quarter.service';
import { Quarter } from '../shared/types/model/Quarter';
import { BehaviorSubject } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import {getQuarterLabel, getValueFromQuery} from '../shared/common';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-quarter-filter',
  templateUrl: './quarter-filter.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class QuarterFilterComponent implements OnInit {
  quarters: BehaviorSubject<Quarter[]> = new BehaviorSubject<Quarter[]>([]);
  @Output() quarterLabel$ = new EventEmitter<string>();
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
        this.quarterId = quarters[1].id;
        if (quarterQuery !== undefined) {
          this.changeDisplayedQuarter();
        } else {
          this.refreshDataService.quarterFilterReady.next();
        }
      }
      const quarterLabel = quarters.find((e) => e.id == this.quarterId)?.label || '';
      this.quarterLabel$.next(quarterLabel);
    });
  }

  changeDisplayedQuarter() {
    const id = this.quarterId;
    const quarterLabel = this.quarters.getValue().find((e) => e.id == id)?.label || '';
    this.quarterLabel$.next(quarterLabel);

    this.router
      .navigate([], { queryParams: { quarter: id } })
      .then(() => this.refreshDataService.quarterFilterReady.next());
  }

  protected readonly getQuarterLabel = getQuarterLabel;
}
