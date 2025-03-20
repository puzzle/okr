import { ChangeDetectionStrategy, Component, EventEmitter, OnInit, Output } from '@angular/core';
import { QuarterService } from '../../../services/quarter.service';
import { Quarter } from '../../types/model/quarter';
import { BehaviorSubject, forkJoin } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { RefreshDataService } from '../../../services/refresh-data.service';
import { getValueFromQuery } from '../../common';

@Component({
  selector: 'app-quarter-filter',
  templateUrl: './quarter-filter.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class QuarterFilterComponent implements OnInit {
  quarters: BehaviorSubject<Quarter[]> = new BehaviorSubject<Quarter[]>([]);

  @Output() quarterLabel$ = new EventEmitter<string>();

  currentQuarterId = -1;

  constructor(
    private quarterService: QuarterService,
    private router: Router,
    private route: ActivatedRoute,
    private refreshDataService: RefreshDataService
  ) {}

  ngOnInit() {
    const allQuarters$ = this.quarterService.getAllQuarters();
    const currentQuarter$ = this.quarterService.getCurrentQuarter();
    forkJoin([allQuarters$,
      currentQuarter$])
      .subscribe(([quarters,
        currentQuarter]) => {
        this.quarters.next(quarters);
        const quarterQuery = this.route.snapshot.queryParams['quarter'];
        const quarterId: number = getValueFromQuery(quarterQuery)[0];
        if (quarters.map((quarter) => quarter.id)
          .includes(quarterId)) {
          this.currentQuarterId = quarterId;
          this.changeDisplayedQuarter();
        } else {
          this.currentQuarterId = currentQuarter.id;
          this.changeDisplayedQuarter();

          if (quarterQuery === undefined) {
            this.refreshDataService.quarterFilterReady.next();
          }
        }
        const quarterLabel = quarters.find((e) => e.id == this.currentQuarterId)?.label || '';
        this.quarterLabel$.next(quarterLabel);
      });
  }

  changeDisplayedQuarter() {
    const id = this.currentQuarterId;
    const quarterLabel = this.quarters.getValue()
      .find((e) => e.id == id)?.label || '';
    this.quarterLabel$.next(quarterLabel);

    this.router
      .navigate([], { queryParams: { quarter: id } })
      .then(() => this.refreshDataService.quarterFilterReady.next());
  }
}
