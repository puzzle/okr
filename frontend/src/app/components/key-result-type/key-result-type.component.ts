import { AfterContentInit, Component, Input } from '@angular/core';
import { KeyResult } from '../../shared/types/model/key-result';
import { ControlContainer, FormGroup, FormGroupDirective } from '@angular/forms';
import { KeyResultMetric } from '../../shared/types/model/key-result-metric';
import { KeyResultOrdinal } from '../../shared/types/model/key-result-ordinal';
import { Unit } from '../../shared/types/enums/unit';
import { formInputCheck, hasFormFieldErrors } from '../../shared/common';
import { getFullNameOfUser, User } from '../../shared/types/model/user';
import { Observable, Subject } from 'rxjs';

export enum KeyResultMetricField {
  BASELINE,
  TARGETGOAL,
  STRETCHGOAL,
  NONE
}

export interface MetricValue {
  baseline: number;
  targetGoal: number;
  stretchGoal: number;
}

@Component({
  selector: 'app-key-result-type',
  templateUrl: './key-result-type.component.html',
  styleUrls: ['./key-result-type.component.scss'],
  standalone: false,
  viewProviders: [{ provide: ControlContainer,
    useExisting: FormGroupDirective }]
})
export class KeyResultTypeComponent implements AfterContentInit {
  childForm: FormGroup;

  @Input() keyResultForm!: FormGroup;

  @Input() keyResult?: KeyResult;

  @Input() users: Observable<User[]> = new Subject();

  protected readonly Unit = Unit;

  protected readonly formInputCheck = formInputCheck;

  protected readonly hasFormFieldErrors = hasFormFieldErrors;

  constructor(private parentF: FormGroupDirective) {
    this.childForm = this.parentF.form;
  }

  switchKeyResultType(newType: string) {
    if (newType !== this.currentKeyResultType() && this.isTypeChangeAllowed()) {
      this.keyResultForm.get('keyResultType')
        ?.setValue(newType);
    }
  }

  isTypeChangeAllowed() {
    return (this.keyResult as KeyResultMetric | KeyResultOrdinal)?.lastCheckIn?.id == null;
  }

  isMetric() {
    return this.currentKeyResultType() == 'metric';
  }

  currentKeyResultType(): string {
    return this.keyResultForm?.get('keyResultType')?.value;
  }

  updateMetricValue(changed: KeyResultMetricField, value: any) {
    const formGroupMetric = this.keyResultForm.get('metric');
    const formGroupValue = this.getMetricValue(formGroupMetric?.value, value);
    const newMetricValue = this.calculateValueAfterChanged(formGroupValue, changed);
    formGroupMetric?.patchValue(newMetricValue, { emitEvent: false });
  }

  getMetricValue(formGroupValue: any, fieldValue: any): MetricValue {
    formGroupValue = { ...formGroupValue,
      ...fieldValue };
    return { baseline: +formGroupValue.baseline,
      targetGoal: +formGroupValue.targetGoal,
      stretchGoal: +formGroupValue.stretchGoal } as MetricValue;
  }

  calculateValueAfterChanged(values: MetricValue, changed: KeyResultMetricField) {
    switch (changed) {
      case KeyResultMetricField.STRETCHGOAL:
      case KeyResultMetricField.BASELINE: {
        return this.calculateValueForField(values, KeyResultMetricField.TARGETGOAL);
      }
      case KeyResultMetricField.TARGETGOAL: {
        return this.calculateValueForField(values, KeyResultMetricField.STRETCHGOAL);
      }
      case KeyResultMetricField.NONE: {
        return {};
      }
    }
  }

  calculateValueForField(values: MetricValue, field: KeyResultMetricField) {
    switch (field) {
      case KeyResultMetricField.BASELINE: {
        return { baseline: (values.targetGoal - values.stretchGoal * 0.7) / 0.3 };
      }

      case KeyResultMetricField.TARGETGOAL: {
        return { targetGoal: (values.stretchGoal - values.baseline) * 0.7 + values.baseline };
      }

      case KeyResultMetricField.STRETCHGOAL: {
        return { stretchGoal: (values.targetGoal - values.baseline) / 0.7 + values.baseline };
      }

      case KeyResultMetricField.NONE: {
        return {};
      }
    }
  }

  protected readonly getFullNameOfUser = getFullNameOfUser;

  ngAfterContentInit(): void {
    const formGroupMetric = this.keyResultForm.get('metric');
    formGroupMetric?.get('baseline')?.valueChanges.subscribe((value: any) => this.updateMetricValue(KeyResultMetricField.BASELINE, { baseline: value }));
    formGroupMetric?.get('targetGoal')?.valueChanges.subscribe((value) => this.updateMetricValue(KeyResultMetricField.TARGETGOAL, { targetGoal: value }));
    formGroupMetric?.get('stretchGoal')?.valueChanges.subscribe((value) => this.updateMetricValue(KeyResultMetricField.STRETCHGOAL, { stretchGoal: value }));
  }
}

