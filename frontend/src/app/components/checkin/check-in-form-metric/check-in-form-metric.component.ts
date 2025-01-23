import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import { KeyResultMetric } from '../../../shared/types/model/key-result-metric';
import { CheckInMin } from '../../../shared/types/model/check-in-min';
import { formInputCheck, hasFormFieldErrors } from '../../../shared/common';
import { TranslateService } from '@ngx-translate/core';
import { CheckInMetricMin } from '../../../shared/types/model/check-in-metric-min';

@Component({
  selector: 'app-check-in-form-metric',
  templateUrl: './check-in-form-metric.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
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
    this.dialogForm.controls['metricValue'].setValidators([Validators.required,
      Validators.pattern('^\\s*-?\\d+\\.?\\d*\\s*$')]);
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

  getCheckInMetric(): CheckInMetricMin {
    return this.checkIn as CheckInMetricMin;
  }
}
