import { Component, Input, OnInit } from '@angular/core';
import { KeyResult } from '../../shared/types/model/key-result';
import {
  ControlContainer,
  FormGroup,
  FormGroupDirective
} from '@angular/forms';
import { KeyResultMetric } from '../../shared/types/model/key-result-metric';
import { KeyResultOrdinal } from '../../shared/types/model/key-result-ordinal';
import { Unit } from '../../shared/types/enums/unit';
import { formInputCheck, hasFormFieldErrors } from '../../shared/common';
import { TranslateService } from '@ngx-translate/core';
import { getFullNameOfUser, User } from '../../shared/types/model/user';
import { Observable, Subject } from 'rxjs';

@Component({
  selector: 'app-key-result-type',
  templateUrl: './key-result-type.component.html',
  styleUrls: ['./key-result-type.component.scss'],
  standalone: false,
  viewProviders: [{ provide: ControlContainer,
    useExisting: FormGroupDirective }]
})
export class KeyResultTypeComponent implements OnInit {
  childForm: FormGroup;

  @Input() keyResultForm!: FormGroup;

  @Input() keyResult!: KeyResult | null;

  @Input() users: Observable<User[]> = new Subject();

  isMetric = true;

  typeChangeAllowed = true;

  protected readonly Unit = Unit;

  protected readonly formInputCheck = formInputCheck;

  protected readonly hasFormFieldErrors = hasFormFieldErrors;

  constructor(private translate: TranslateService, private parentF: FormGroupDirective) {
    this.childForm = this.parentF.form;
  }

  ngOnInit(): void {
    if (this.keyResult) {
      this.typeChangeAllowed = (this.keyResult as KeyResultMetric | KeyResultOrdinal).lastCheckIn?.id == null;
      this.isMetric = this.keyResult.keyResultType == 'metric';
      this.isMetric
        ? this.keyResultForm.patchValue({ ...this.castToMetric(this.keyResult) })
        : this.keyResultForm.patchValue({ ...this.castToOrdinal(this.keyResult) });
    }
    this.setValidators(this.keyResultForm.value.keyResultType);
  }

  castToMetric(keyResult: KeyResult) {
    return keyResult as KeyResultMetric;
  }

  castToOrdinal(keyResult: KeyResult) {
    return keyResult as KeyResultOrdinal;
  }

  setValidators(type: string) {
    if (type == 'metric') {
      this.keyResultForm.get('metric')
        ?.enable();
      this.keyResultForm.get('ordinal')
        ?.disable();
    }
    if (type == 'ordinal') {
      this.keyResultForm.get('metric')
        ?.disable();
      this.keyResultForm.get('ordinal')
        ?.enable();
    }
  }

  switchKeyResultType(type: string) {
    if ((type == 'metric' && !this.isMetric || type == 'ordinal' && this.isMetric) && this.typeChangeAllowed) {
      this.isMetric = !this.isMetric;
      const keyResultType = this.isMetric ? 'metric' : 'ordinal';
      this.keyResultForm.controls['keyResultType'].setValue(keyResultType);
    }
    this.setValidators(type);
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

  protected readonly getFullNameOfUser = getFullNameOfUser;
}
