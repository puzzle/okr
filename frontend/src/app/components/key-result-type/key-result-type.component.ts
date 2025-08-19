import { AfterContentInit, Component, Input } from '@angular/core';
import { KeyResult } from '../../shared/types/model/key-result';
import { ControlContainer, FormGroup, FormGroupDirective } from '@angular/forms';
import { KeyResultMetric } from '../../shared/types/model/key-result-metric';
import { KeyResultOrdinal } from '../../shared/types/model/key-result-ordinal';
import { Unit } from '../../shared/types/enums/unit';
import { formInputCheck } from '../../shared/common';
import { getFullNameOfUser, User } from '../../shared/types/model/user';
import { map, Observable, startWith, Subject, tap } from 'rxjs';
import { UnitService } from '../../services/unit.service';
import { DialogService } from '../../services/dialog.service';
import { ManageUnitsDialogComponent } from '../manage-units-dialog/manage-units-dialog.component';

export enum KeyResultMetricField {
  BASELINE,
  TARGET_VALUE,
  STRETCH_GOAL,
  NONE
}

export interface MetricValue {
  baseline: number;
  targetValue: number;
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

  protected readonly formInputCheck = formInputCheck;

  unitOptions: Unit[] = [];

  filteredUnitOptions = new Observable<Unit[]>();

  unitSearchTerm = '';

  constructor(private parentF: FormGroupDirective, private unitService: UnitService, private dialogService: DialogService) {
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
    formGroupMetric?.updateValueAndValidity();

    const hasUndefinedValue = Object.values(value)
      .some((v) => v === undefined);
    if (hasUndefinedValue || formGroupMetric?.invalid) {
      return;
    }

    const formGroupValue = this.getMetricValue(formGroupMetric?.value, value);
    const newMetricValue = this.calculateValueAfterChanged(formGroupValue, changed);
    formGroupMetric?.patchValue(newMetricValue, { emitEvent: false });
  }

  getMetricValue(formGroupValue: any, fieldValue: any): MetricValue {
    formGroupValue = { ...formGroupValue,
      ...fieldValue };
    return { baseline: +formGroupValue.baseline,
      targetValue: +formGroupValue.targetValue,
      commitValue: +formGroupValue.commitValue,
      stretchGoal: +formGroupValue.stretchGoal } as MetricValue;
  }

  calculateValueAfterChanged(values: MetricValue, changed: KeyResultMetricField) {
    switch (changed) {
      case KeyResultMetricField.STRETCH_GOAL:
      case KeyResultMetricField.BASELINE: {
        return this.calculateValueForField(values, KeyResultMetricField.TARGET_VALUE);
      }
      case KeyResultMetricField.TARGET_VALUE: {
        return this.calculateValueForField(values, KeyResultMetricField.STRETCH_GOAL);
      }

      case KeyResultMetricField.NONE: {
        return {};
      }
    }
  }

  calculateValueForField(values: MetricValue, field: KeyResultMetricField) {
    const roundToTwoDecimals = (num: number) => parseFloat(num.toFixed(2));

    switch (field) {
      case KeyResultMetricField.BASELINE: {
        return { baseline: roundToTwoDecimals((values.targetValue - values.stretchGoal * 0.7) / 0.3) };
      }

      case KeyResultMetricField.TARGET_VALUE: {
        return { targetValue: roundToTwoDecimals((values.stretchGoal - values.baseline) * 0.7 + values.baseline) };
      }

      case KeyResultMetricField.STRETCH_GOAL: {
        return { stretchGoal: roundToTwoDecimals((values.targetValue - values.baseline) / 0.7 + values.baseline) };
      }

      case KeyResultMetricField.NONE: {
        return {};
      }
    }
  }

  protected readonly getFullNameOfUser = getFullNameOfUser;


  displayFn(unit: Unit): string {
    const capitalize = (s: string) => s.charAt(0)
      .toUpperCase() + s.slice(1)
      .toLowerCase();
    const s = unit?.unitName || '';
    return s.length > 3 ? capitalize(s) : s;
  }

  ngAfterContentInit(): void {
    const formGroupMetric = this.keyResultForm.get('metric');
    this.updateMetricValue(KeyResultMetricField.STRETCH_GOAL, { stretchGoal: formGroupMetric?.get('stretchGoal')?.value });

    formGroupMetric?.get('baseline')?.valueChanges
      .subscribe((value: any) => this.updateMetricValue(KeyResultMetricField.BASELINE, { baseline: value }));
    formGroupMetric?.get('targetValue')?.valueChanges
      .subscribe((value) => this.updateMetricValue(KeyResultMetricField.TARGET_VALUE, { targetValue: value }));
    formGroupMetric?.get('stretchGoal')?.valueChanges
      .subscribe((value) => this.updateMetricValue(KeyResultMetricField.STRETCH_GOAL, { stretchGoal: value }));

    this.filteredUnitOptions = formGroupMetric!.get('unit')!.valueChanges.pipe(startWith(''), tap((value) => {
      if (typeof value !== 'string') {
        return;
      }
      this.unitSearchTerm = value;
    }), map((value) => this.filter(value || '')));
  }

  createNewUnit() {
    this.keyResultForm.get('metric')
      ?.get('unit')
      ?.setValue({ unitName: this.unitSearchTerm });
    this.keyResultForm.get('metric')
      ?.get('unit')
      ?.updateValueAndValidity();
    this.unitService.checkForNewUnit(this.unitSearchTerm)
      .subscribe((result: Unit) => this.unitService.createUnit(result)
        .subscribe((unit) => {
          this.unitOptions.push(unit);
          this.keyResultForm.get('metric')
            ?.get('unit')
            ?.setValue(unit);
          this.keyResultForm.get('metric')
            ?.get('unit')
            ?.updateValueAndValidity();
        }));
  }

  private filter(value: string): Unit[] {
    const filterValue = value.toString()
      .toLowerCase();
    return this.unitOptions.filter((option) => option.unitName.toLowerCase()
      .includes(filterValue));
  }

  setUnits() {
    this.unitService.getUnits()
      .subscribe((units) => {
        this.unitOptions = units;
        const currentValue = this.keyResultForm.get('metric')
          ?.get('unit')?.value;
        if (currentValue.id) {
          const find = this.unitOptions.find((unit) => unit.id === currentValue.id);
          if (find) {
            this.keyResultForm.get('metric')
              ?.get('unit')
              ?.setValue(find);
          } else {
            this.keyResultForm.get('metric')
              ?.get('unit')
              ?.reset();
          }
          this.keyResultForm.get('metric')
            ?.get('unit')
            ?.updateValueAndValidity();
        }
      });
  }

  canCreate(options: Unit[]) {
    const rawValue = this.keyResultForm.get('metric')
      ?.get('unit')
      ?.getRawValue();
    const value = rawValue?.unitName || rawValue || '';
    const doesSearchAlreadyExist = this.unitOptions.some((option) => option.unitName.toLowerCase()
      .trim() === value.toLowerCase()
      .trim());
    return options.length === 0 && !doesSearchAlreadyExist;
  }

  openManageUnitsDialog() {
    this.dialogService.open(ManageUnitsDialogComponent)
      .afterClosed()
      .subscribe(() => {
        this.setUnits();
      });
  }
}

