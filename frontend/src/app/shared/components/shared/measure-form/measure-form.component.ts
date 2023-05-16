import { Component, OnInit } from '@angular/core';
import { combineLatest, filter, map, Observable, of } from 'rxjs';
import { KeyResultMeasure, KeyResultService } from '../../../services/key-result.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { getNumberOrNull } from '../../../common';
import { Measure, MeasureService } from '../../../services/measure.service';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { Goal, GoalService } from '../../../services/goal.service';
import { QuarterService, StartEndDateDTO } from '../../../services/quarter.service';
import { RouteService } from '../../../services/route.service';

@Component({
  selector: 'app-measure-form',
  templateUrl: './measure-form.component.html',
  styleUrls: ['./measure-form.component.scss'],
})
export class MeasureFormComponent implements OnInit {
  measure$!: Observable<Measure>;
  keyResultUnit!: string;
  startEndDate$!: Observable<StartEndDateDTO>;

  measureForm = new FormGroup({
    value: new FormControl<number>(0, [Validators.required]),
    measureDate: new FormControl<Date>(new Date(), [Validators.required]),
    changeInfo: new FormControl<string>('', [Validators.required, Validators.maxLength(250)]),
    initiatives: new FormControl<string>('', [Validators.maxLength(4096)]),
  });
  public keyresult$!: Observable<KeyResultMeasure>;
  public goal$!: Observable<Goal>;
  public create!: boolean;

  constructor(
    private keyResultService: KeyResultService,
    private route: ActivatedRoute,
    private router: Router,
    private measureService: MeasureService,
    private toastr: ToastrService,
    private location: Location,
    private goalService: GoalService,
    private quarterService: QuarterService,
    private routeService: RouteService
  ) {}

  ngOnInit(): void {
    let keyResultId = getNumberOrNull(this.route.snapshot.paramMap.get('keyresultId'));

    let measureId = getNumberOrNull(this.route.snapshot.paramMap.get('measureId'));

    this.startEndDate$ = this.quarterService.getStartAndEndDateOfKeyresult(keyResultId!);
    this.create = !measureId;
    if (keyResultId) {
      this.keyresult$ = this.keyResultService.getKeyResultById(keyResultId);
      this.keyresult$.subscribe((keyResult) => {
        this.keyResultUnit = keyResult.unit;
      });
      this.goal$ = this.goalService.getGoalByKeyResultId(keyResultId);
    }

    if (measureId) {
      this.measure$ = this.measureService.getMeasureById(measureId);
    } else {
      let measure = this.measureService.getInitMeasure();
      measure.keyResultId = keyResultId!;
      this.measure$ = of(measure);
    }

    combineLatest([this.measure$, this.goal$, this.startEndDate$])
      .pipe(
        filter(([measure, goal]) => measure !== null || goal !== null),
        map(([measure, goal, startEndDate]) => {
          let value;
          if (measure.id) {
            value = measure.value;
          } else if (goal.value !== null) {
            value = goal.value;
          } else if (goal.basicValue !== null) {
            value = goal.basicValue;
          } else {
            value = null;
          }
          return {
            value: value,
            measureDate: this.create
              ? this.returnQuarterEndDateOrCurrentDate(new Date(startEndDate.endDate), measure?.measureDate)
              : measure?.measureDate,
            changeInfo: measure?.changeInfo,
            initiatives: measure?.initiatives,
          };
        })
      )
      .subscribe((formValue) => {
        this.measureForm.setValue(formValue);
      });
  }

  returnQuarterEndDateOrCurrentDate(endDate: Date, measureDate: Date) {
    if (measureDate > endDate) {
      return endDate;
    } else {
      return measureDate;
    }
  }

  save() {
    this.measure$
      .pipe(
        map((measure) => {
          return {
            ...measure,
            ...this.measureForm.value,
          } as Measure;
        })
      )
      .subscribe((measure) => {
        measure.measureDate = new Date(measure.measureDate);
        measure.measureDate.setHours(0, 0, 0, 0);
        this.measureService.saveMeasure(measure, this.create).subscribe({
          next: () => {
            this.toastr.success('', 'Messung gespeichert!', {
              timeOut: 5000,
            });
            this.routeService.navigate('/keyresults/' + measure.keyResultId);
          },
          error: (e: HttpErrorResponse) => {
            this.toastr.error(e.error.message, 'Fehlerstatus: ' + e.status, {
              timeOut: 5000,
            });
            console.log('Can not save this measure: ', measure);
            return new Error('ups sommething happend');
          },
        });
      });
  }

  navigateBack() {
    this.routeService.back();
  }
}
