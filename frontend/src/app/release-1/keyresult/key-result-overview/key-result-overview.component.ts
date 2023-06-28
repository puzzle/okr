import { Component, Input, OnInit } from '@angular/core';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import { Observable } from 'rxjs';
import { QuarterService, StartEndDateDTO } from '../../shared/services/quarter.service';
import { DatePipe } from '@angular/common';
import { RouteService } from '../../shared/services/route.service';

@Component({
  selector: 'app-key-result-overview',
  templateUrl: './key-result-overview.component.html',
  styleUrls: ['./key-result-overview.component.scss'],
})
export class KeyResultOverviewComponent implements OnInit {
  @Input() keyResult!: KeyResultMeasure;
  startEndDate$!: Observable<StartEndDateDTO>;

  constructor(private quarterService: QuarterService, private datePipe: DatePipe, public routeService: RouteService) {}

  ngOnInit(): void {
    this.startEndDate$ = this.quarterService.getStartAndEndDateOfKeyresult(this.keyResult.id!);
  }

  formatDate(date: string) {
    let convertedDate: Date = new Date(date);
    return this.datePipe.transform(convertedDate, 'dd.MM.yyyy', 'CEST');
  }
}
