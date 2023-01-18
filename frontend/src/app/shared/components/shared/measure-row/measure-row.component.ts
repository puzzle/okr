import { Component, OnInit } from '@angular/core';
import { Observable, switchMap } from 'rxjs';
import {
  KeyResultService,
  Measure,
} from '../../../services/key-result.service';
import { getNumberOrNull } from '../../../common';
import { ActivatedRoute } from '@angular/router';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-measure-row',
  templateUrl: './measure-row.component.html',
  styleUrls: ['./measure-row.component.scss'],
})
export class MeasureRowComponent implements OnInit {
  measures$!: Observable<Measure[]>;

  constructor(
    private keyresultService: KeyResultService,
    private route: ActivatedRoute,
    private datePipe: DatePipe
  ) {}

  ngOnInit(): void {
    this.measures$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const keyResultId = getNumberOrNull(params.get('keyresultId'));
        if (keyResultId) {
          return this.keyresultService.getMeasuresOfKeyResult(keyResultId);
        } else {
          throw Error('KeyResult with Id ' + keyResultId + " doesn't exist");
        }
      })
    );
  }

  formatDate(date: string) {
    var convertedDate: Date = new Date(date);
    return this.datePipe.transform(convertedDate, 'dd.MM.yyyy', 'CEST');
  }
}
