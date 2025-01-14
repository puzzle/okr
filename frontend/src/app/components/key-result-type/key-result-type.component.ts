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

  @Input() keyResult?: KeyResult;

  @Input() users: Observable<User[]> = new Subject();

  protected readonly Unit = Unit;

  protected readonly formInputCheck = formInputCheck;

  protected readonly hasFormFieldErrors = hasFormFieldErrors;

  constructor(private translate: TranslateService, private parentF: FormGroupDirective) {
    this.childForm = this.parentF.form;
  }

  ngOnInit(): void {
    if (!this.keyResult) {
      return;
    }

    this.keyResultForm.patchValue({ ...this.keyResult });

    if (this.currentKeyResultType() == 'metric') {
      const keyResultMetric = this.castToMetric(this.keyResult);
      const krUnit = Unit[keyResultMetric.unit as keyof typeof Unit];
      this.keyResultForm.get('metric')
        ?.patchValue({ ...keyResultMetric,
          targetGoal: 70,
          unit: krUnit });
    }

    if (this.currentKeyResultType() == 'ordinal') {
      this.keyResultForm.get('ordinal')
        ?.patchValue({ ...this.castToOrdinal(this.keyResult) });
    }

    // this.setValidators(this.keyResultForm.value.keyResultType);
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

  switchKeyResultType(newType: string) {
    if (newType !== this.currentKeyResultType() && this.isTypeChangeAllowed()) {
      this.keyResultForm.get('keyResultType')
        ?.setValue(newType);
    }
    this.setValidators(newType);
  }

  getErrorMessage(
    error: string, field: string, firstNumber: number | null, secondNumber: number | null
  ): string {
    return field + this.translate.instant('DIALOG_ERRORS.' + error)
      .format(firstNumber, secondNumber);
  }

  /*
   * getErrorMessage(formfield: any) {
   *   if (!formfield.errors) {
   *     return;
   *   }
   *   return Object.keys(formfield.errors).map((errorKey: any) =>
   *       this.translate.instant(
   *           'ERRORS.' + errorKey,
   *           formfield.errors[errorKey],
   *       ),
   *   );
   * }
   */


  invalidOwner(): boolean {
    // return (this.isTouchedOrDirty('owner') && (typeof this.keyResultForm.value.owner === 'string' || !this.keyResultForm.value.owner));
    return false;
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

  protected readonly getFullNameOfUser = getFullNameOfUser;
}
