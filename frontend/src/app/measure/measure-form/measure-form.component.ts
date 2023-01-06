import { Component, OnInit } from '@angular/core';
import { map, Observable, switchMap } from 'rxjs';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../shared/services/key-result.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { getNumberOrNull } from '../../shared/common';
import { Measure, MeasureService } from '../../shared/services/measure.service';

@Component({
  selector: 'app-measure-form',
  templateUrl: './measure-form.component.html',
  styleUrls: ['./measure-form.component.scss'],
})
export class MeasureFormComponent implements OnInit {
  measure$!: Observable<Measure>;

  measureForm = new FormGroup({
    value: new FormControl<number>(0, [
      Validators.required,
      Validators.pattern('^[0-9]*$'),
    ]),
    measureDate: new FormControl<Date>(new Date(), [Validators.required]),
    changeInfo: new FormControl<string>('', [Validators.required]),
    initiatives: new FormControl<string>(''),
  });
  public keyresult$!: Observable<KeyResultMeasure>;
  public create!: boolean;

  constructor(
    private keyResultService: KeyResultService,
    private route: ActivatedRoute,
    private router: Router,
    private measureService: MeasureService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.keyresult$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const keyresultId = getNumberOrNull(params.get('keyresultId'));
        if (keyresultId) {
          return this.keyResultService.getKeyResultById(keyresultId);
        } else {
          throw Error('KeyResult with Id ' + keyresultId + " doesn't exist");
        }
      })
    );
    this.measure$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const measureId = getNumberOrNull(params.get('measureId'));
        if (measureId) {
          this.create = false;
          this.keyresult$.subscribe((keyresult) => {
            if (keyresult.unit == 'BINARY') {
              this.measureForm
                .get('value')
                ?.addValidators(Validators.pattern('^[0-1]{1}$'));
            } else if (keyresult.unit == 'PERCENT') {
              this.measureForm
                .get('value')
                ?.addValidators(Validators.pattern('^[0-9][0-9]?$|^100$'));
            }
          });

          return this.measureService.getMeasureById(measureId);
        } else {
          this.create = true;
          let measure: Measure = this.measureService.getInitMeasure();
          return this.keyresult$.pipe(
            map((keyresult) => {
              measure.keyResultId = keyresult.id!;
              // measure.createdBy = loggedInUser
              if (keyresult.unit == 'BINARY') {
                this.measureForm
                  .get('value')
                  ?.addValidators(Validators.pattern('^[0-1]{1}$'));
              } else if (keyresult.unit == 'PERCENT') {
                this.measureForm
                  .get('value')
                  ?.addValidators(Validators.pattern('^[0-9][0-9]?$|^100$'));
              }

              return measure;
            })
          );
        }
      })
    );

    this.measure$.subscribe((measure) => {
      const { id, keyResultId, createdBy, createdOn, ...restMeasure } = measure;
      restMeasure.measureDate = new Date(measure.measureDate);
      this.measureForm.setValue(restMeasure);
    });
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
        measure.measureDate = new Date(measure.measureDate.toString() + 'UTC');
        this.measureService.saveMeasure(measure, this.create).subscribe({
          next: () => this.router.navigate(['/dashboard']),
          error: () => {
            console.log('Can not save this measure: ', measure);
            return new Error('ups sommething happend');
          },
        });
      });
  }

  navigateBack() {
    this.location.back();
  }
}
