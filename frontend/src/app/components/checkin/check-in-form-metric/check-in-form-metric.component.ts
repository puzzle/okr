import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import { KeyResultMetric } from '../../../shared/types/model/KeyResultMetric';
import { CheckInMin, CheckInMinMetric, CheckInMinOrdinal } from '../../../shared/types/model/CheckInMin';
import { formInputCheck, hasFormFieldErrors } from '../../../shared/common';
import { TranslateService } from '@ngx-translate/core';

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
  protected readonly formInputCheck = formInputCheck;
  protected readonly hasFormFieldErrors = hasFormFieldErrors;

  constructor(private translate: TranslateService) {}

  ngOnInit() {
    this.dialogForm.controls['value'].setValidators([Validators.required, Validators.pattern('^-?\\d+\\.?\\d*$')]);
  }

  generateUnitLabel(): string {
    switch (this.keyResult.unit) {
      case 'PERCENT':
        return '%';
      case 'CHF':
        return 'CHF';
      case 'EUR':
        return 'EUR';
      case 'FTE':
        return 'FTE';
      default:
        return '';
    }
  }

  getErrorMessage(error: string, field: string): string {
    return field + this.translate.instant('DIALOG_ERRORS.' + error);
  }

  getCheckInMetric(): CheckInMinMetric {
    return this.checkIn as CheckInMinMetric;
  }
}
