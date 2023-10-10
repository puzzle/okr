import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { QuarterService } from '../shared/services/quarter.service';
import { Quarter } from '../shared/types/model/Quarter';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'app-quarter-filter',
  templateUrl: './quarter-filter.component.html',
  styleUrls: ['./quarter-filter.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class QuarterFilterComponent implements OnInit {
  quarters$: Observable<Quarter[]> = of([]);

  constructor(private quarterService: QuarterService) {}

  ngOnInit() {
    this.quarters$ = this.quarterService.getAllQuarters();
  }
}
