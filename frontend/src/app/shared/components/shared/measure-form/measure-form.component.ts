import { Component, OnInit } from '@angular/core';
import { map, Observable, of } from 'rxjs';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../../services/key-result.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { getNumberOrNull } from '../../../common';
import { Measure, MeasureService } from '../../../services/measure.service';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { Goal, GoalService } from '../../../services/goal.service';

@Component({
  selector: 'app-measure-form',
  templateUrl: './measure-form.component.html',
  styleUrls: ['./measure-form.component.scss'],
})
export class MeasureFormComponent implements OnInit {
  measure$!: Observable<Measure>;
  keyResultUnit!: string;

  measureForm = new FormGroup({
    value: new FormControl<number | boolean>(0, [Validators.required]),
    measureDate: new FormControl<Date>(new Date(), [Validators.required]),
    changeInfo: new FormControl<string>('', [Validators.required]),
    initiatives: new FormControl<string>(''),
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
    private goalService: GoalService
  ) {}

  ngOnInit(): void {
    let keyResultId = getNumberOrNull(
      this.route.snapshot.paramMap.get('keyresultId')
    );

    let measureId = getNumberOrNull(
      this.route.snapshot.paramMap.get('measureId')
    );

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

    this.measure$.subscribe((measure) => {
      this.measureForm.setValue({
        value: this.correctValueForBinaryDataToSlider(measure.value),
        measureDate: measure.measureDate,
        changeInfo: measure.changeInfo,
        initiatives: measure.initiatives,
      });
    });
  }

  correctValueForBinaryDataToSlider(measureValue: number | boolean) {
    if (this.keyResultUnit === 'BINARY') {
      measureValue === 1 ? (measureValue = true) : (measureValue = false);
    }
    return measureValue;
  }

  correctValueForBinarySliderToData() {
    if (this.keyResultUnit === 'BINARY') {
      if (this.measureForm.get('value')?.value === true) {
        this.measureForm.get('value')?.setValue(1);
      } else {
        this.measureForm.get('value')?.setValue(0);
      }
    }
  }

  save() {
    this.correctValueForBinarySliderToData();
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
        measure.measureDate = new Date(measure.measureDate.toString() + 'UTC');
        this.measureService.saveMeasure(measure, this.create).subscribe({
          next: () => {
            this.toastr.success('', 'Messung gespeichert!', {
              timeOut: 5000,
            });
            this.router.navigate(['/keyresults/' + measure.keyResultId]);
          },
          error: (e: HttpErrorResponse) => {
            this.toastr.error(
              'Messung konnte nicht gespeichert werden!',
              'Fehlerstatus: ' + e.status,
              {
                timeOut: 5000,
              }
            );
            console.log('Can not save this measure: ', measure);
            return new Error('ups sommething happend');
          },
        });
      });
  }

  navigateBack() {
    this.create ? this.router.navigate(['/dashboard']) : this.location.back();
  }
}
