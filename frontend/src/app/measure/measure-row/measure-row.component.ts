import { Component, OnInit } from '@angular/core';
import { Observable, switchMap } from 'rxjs';
import {
  KeyResultService,
  Measure,
} from '../../shared/services/key-result.service';
import { getNumberOrNull } from '../../shared/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-measure-row',
  templateUrl: './measure-row.component.html',
  styleUrls: ['./measure-row.component.scss'],
})
export class MeasureRowComponent implements OnInit {
  measures$!: Observable<Measure[]>;

  constructor(
    private keyresultService: KeyResultService,
    private route: ActivatedRoute
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

    this.measures$.subscribe((measures) => {
      console.log(measures);
      console.log(measures[0].createdBy);
    });
  }
}
