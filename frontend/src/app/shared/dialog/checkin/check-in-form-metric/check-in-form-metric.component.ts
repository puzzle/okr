import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import errorMessages from '../../../../../assets/errors/error-messages.json';
import { KeyResultMetric } from '../../../types/model/KeyResultMetric';
import { CheckInMin } from '../../../types/model/CheckInMin';
import { formInputCheck } from '../../../common';

@Component({
  selector: 'app-check-in-form-metric',
  templateUrl: './check-in-form-metric.component.html',
  styleUrls: ['./check-in-form-metric.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CheckInFormMetricComponent implements OnInit {
  @Input()
  keyResult!: KeyResultMetric;
  @Input()
  checkIn!: CheckInMin;
  @Input()
  dialogForm!: FormGroup;
  protected readonly errorMessages: any = errorMessages;
  protected readonly formInputCheck = formInputCheck;

  ngOnInit() {
    this.dialogForm.controls['value'].setValidators([Validators.required, Validators.pattern('^-?\\d+\\.?\\d*$')]);
  }

  generateUnitLabel(): string {
    switch (this.keyResult.unit) {
      case 'PERCENT':
        return '%';
      case 'CHF':
        return 'CHF';
      case 'FTE':
        return 'FTE';
      default:
        return '';
    }
  }

  isTouchedOrDirty(name: string) {
    return this.dialogForm.get(name)?.dirty || this.dialogForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.dialogForm.get(name)?.errors;
    return errors === null ? [] : Object.keys(errors!);
  }
}
