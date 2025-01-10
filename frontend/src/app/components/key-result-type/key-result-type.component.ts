import { Component, Input, OnInit } from '@angular/core';
import { KeyResult } from '../../shared/types/model/key-result';
import { FormGroup, Validators } from '@angular/forms';
import { KeyResultMetric } from '../../shared/types/model/key-result-metric';
import { KeyResultOrdinal } from '../../shared/types/model/key-result-ordinal';
import { Unit } from '../../shared/types/enums/unit';
import { formInputCheck, hasFormFieldErrors } from '../../shared/common';
import { TranslateService } from '@ngx-translate/core';
import { User } from '../../shared/types/model/user';
import { Observable, Subject } from 'rxjs';

@Component({
  selector: 'app-key-result-type',
  templateUrl: './key-result-type.component.html',
  styleUrls: ['./key-result-type.component.scss'],
  standalone: false
})
export class KeyResultTypeComponent implements OnInit {
  @Input() keyResultForm!: FormGroup;

  @Input() keyResult!: KeyResult | null;

  @Input() users: Observable<User[]> = new Subject();


  isMetric = true;

  typeChangeAllowed = true;

  protected readonly Unit = Unit;

  protected readonly formInputCheck = formInputCheck;

  protected readonly hasFormFieldErrors = hasFormFieldErrors;

  constructor(private translate: TranslateService) {
  }

  ngOnInit(): void {
    if (this.keyResult) {
      this.typeChangeAllowed = (this.keyResult as KeyResultMetric | KeyResultOrdinal).lastCheckIn?.id == null;
      this.isMetric = this.keyResult.keyResultType == 'metric';
      this.isMetric
        ? this.keyResultForm.patchValue({ ...this.castToMetric(this.keyResult) })
        : this.keyResultForm.patchValue({ ...this.castToOrdinal(this.keyResult) });
    }
    this.switchValidators();
  }

  castToMetric(keyResult: KeyResult) {
    return keyResult as KeyResultMetric;
  }

  castToOrdinal(keyResult: KeyResult) {
    return keyResult as KeyResultOrdinal;
  }

  switchValidators() {
    if (this.isMetric) {
      this.setValidatorsMetric();
      this.clearValidatorsOrdinal();
      this.keyResultForm.updateValueAndValidity();
    } else {
      this.setValidatorsOrdinal();
      this.clearValidatorsMetric();
      this.keyResultForm.updateValueAndValidity();
    }
    this.updateFormValidity();
  }

  async updateFormValidity() {
    await new Promise((r) => setTimeout(r, 100));
  }

  setValidatorsMetric() {
    this.keyResultForm.controls['unit'].setValidators([Validators.required]);
    this.keyResultForm.controls['baseline'].setValidators([Validators.required,
      Validators.pattern('^-?\\d+\\.?\\d*$')]);
    this.keyResultForm.controls['stretchGoal'].setValidators([Validators.required,
      Validators.pattern('^-?\\d+\\.?\\d*$')]);
  }

  setValidatorsOrdinal() {
    this.keyResultForm.controls['commitZone'].setValidators([Validators.required,
      Validators.maxLength(400)]);
    this.keyResultForm.controls['targetZone'].setValidators([Validators.required,
      Validators.maxLength(400)]);
    this.keyResultForm.controls['stretchZone'].setValidators([Validators.required,
      Validators.maxLength(400)]);
  }

  clearValidatorsMetric() {
    this.keyResultForm.controls['unit'].clearValidators();
    this.keyResultForm.controls['baseline'].clearValidators();
    this.keyResultForm.controls['stretchGoal'].clearValidators();
  }

  clearValidatorsOrdinal() {
    this.keyResultForm.controls['commitZone'].clearValidators();
    this.keyResultForm.controls['targetZone'].clearValidators();
    this.keyResultForm.controls['stretchZone'].clearValidators();
  }

  switchKeyResultType(type: string) {
    if ((type == 'metric' && !this.isMetric || type == 'ordinal' && this.isMetric) && this.typeChangeAllowed) {
      this.isMetric = !this.isMetric;
      const keyResultType = this.isMetric ? 'metric' : 'ordinal';
      this.keyResultForm.controls['keyResultType'].setValue(keyResultType);
      this.switchValidators();
    }
  }

  getErrorMessage(
    error: string, field: string, firstNumber: number | null, secondNumber: number | null
  ): string {
    return field + this.translate.instant('DIALOG_ERRORS.' + error)
      .format(firstNumber, secondNumber);
  }

  isTouchedOrDirty(name: string) {
    return this.keyResultForm.get(name)?.dirty || this.keyResultForm.get(name)?.touched;
  }

  invalidOwner(): boolean {
    return (
      !!this.isTouchedOrDirty('owner') &&
      (typeof this.keyResultForm.value.owner === 'string' || !this.keyResultForm.value.owner)
    );
  }

  protected readonly User = User;
}
