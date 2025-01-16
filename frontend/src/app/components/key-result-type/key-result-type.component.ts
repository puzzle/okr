import { AfterContentInit, Component, Input } from '@angular/core';
import { KeyResult } from '../../shared/types/model/key-result';
import { ControlContainer, FormGroup, FormGroupDirective } from '@angular/forms';
import { KeyResultMetric } from '../../shared/types/model/key-result-metric';
import { KeyResultOrdinal } from '../../shared/types/model/key-result-ordinal';
import { Unit } from '../../shared/types/enums/unit';
import { formInputCheck, hasFormFieldErrors } from '../../shared/common';
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

  updateMetricValue(value: any) {
    console.log(value);
  }

  protected readonly getFullNameOfUser = getFullNameOfUser;

  ngAfterContentInit(): void {
    this.keyResultForm.get('metric')?.valueChanges.subscribe((value: any) => this.updateMetricValue(value));
  }
}
