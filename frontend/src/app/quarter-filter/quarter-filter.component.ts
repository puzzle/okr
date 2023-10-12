import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { QuarterService } from '../shared/services/quarter.service';
import { Quarter } from '../shared/types/model/Quarter';
import { Subject } from 'rxjs';
import { Router } from '@angular/router';
import { NotifierService } from '../shared/services/notifier.service';

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
    private notifierService: NotifierService,
  ) {}

  ngOnInit() {
    this.quarterService.getAllQuarters().subscribe((quarters) => {
      this.quarters.next(quarters);
      const urlSearchParam = new URLSearchParams(window.location.search);
      const quarterId = urlSearchParam.get('quarter');
      if (
        quarterId &&
        quarters
          .map((quarter) => {
            return quarter.id;
          })
          .includes(Number(quarterId))
      ) {
        this.quarterId = Number(quarterId);
      } else {
        this.quarterId = quarters[0].id;
        this.changeDisplayedQuarter();
      }
    });
  }

  changeDisplayedQuarter() {
    const id = this.quarterId;
    this.router.navigate([], { queryParams: { quarter: id } }).then(() => {
      this.notifierService.reloadOverview.next(null);
    });
  }
}
