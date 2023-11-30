import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { KeyResult } from '../shared/types/model/KeyResult';
import { FormGroup, Validators } from '@angular/forms';
import { KeyResultMetric } from '../shared/types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../shared/types/model/KeyResultOrdinal';
import { Unit } from '../shared/types/enums/Unit';
import { formInputCheck, hasFormFieldErrors } from '../shared/common';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-keyresult-type',
  templateUrl: './keyresult-type.component.html',
  styleUrls: ['./keyresult-type.component.scss'],
})
export class KeyresultTypeComponent implements OnInit {
  @Input() keyResultForm!: FormGroup;
  @Input() keyresult!: KeyResult | null;
  @Output() formValidityEmitter = new EventEmitter<boolean>();
  isMetric: boolean = true;
  typeChangeAllowed: boolean = true;
  protected readonly Unit = Unit;
  protected readonly formInputCheck = formInputCheck;
  protected readonly hasFormFieldErrors = hasFormFieldErrors;

  constructor(private translate: TranslateService) {}

  ngOnInit(): void {
    if (this.keyresult) {
      this.typeChangeAllowed = this.keyresult.lastCheckIn?.id == null;
      this.isMetric = this.keyresult.keyResultType == 'metric';
      this.isMetric
        ? this.keyResultForm.patchValue({ ...this.castToMetric(this.keyresult) })
        : this.keyResultForm.patchValue({ ...this.castToOrdinal(this.keyresult) });
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
    this.formValidityEmitter.emit(this.keyResultForm.invalid);
  }

  setValidatorsMetric() {
    this.keyResultForm.controls['unit'].setValidators([Validators.required]);
    this.keyResultForm.controls['baseline'].setValidators([
      Validators.required,
      Validators.pattern('^-?\\d+\\.?\\d*$'),
    ]);
    this.keyResultForm.controls['stretchGoal'].setValidators([
      Validators.required,
      Validators.pattern('^-?\\d+\\.?\\d*$'),
    ]);
  }

  setValidatorsOrdinal() {
    this.keyResultForm.controls['commitZone'].setValidators([Validators.required, Validators.maxLength(400)]);
    this.keyResultForm.controls['targetZone'].setValidators([Validators.required, Validators.maxLength(400)]);
    this.keyResultForm.controls['stretchZone'].setValidators([Validators.required, Validators.maxLength(400)]);
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
    if (((type == 'metric' && !this.isMetric) || (type == 'ordinal' && this.isMetric)) && this.typeChangeAllowed) {
      this.isMetric = !this.isMetric;
      let keyResultType = this.isMetric ? 'metric' : 'ordinal';
      this.keyResultForm.controls['keyResultType'].setValue(keyResultType);
      this.switchValidators();
    }
  }

  getErrorMessage(error: string, field: string, firstNumber: number | null, secondNumber: number | null): string {
    return field + this.translate.instant('DIALOG_ERRORS.' + error).format(firstNumber, secondNumber);
  }
}
