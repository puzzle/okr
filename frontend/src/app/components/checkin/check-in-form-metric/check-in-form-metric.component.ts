import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { KeyResultMetric } from '../../../shared/types/model/key-result-metric';
import { CheckInMin } from '../../../shared/types/model/check-in-min';
import { formInputCheck } from '../../../shared/common';
import { CheckInMetricMin } from '../../../shared/types/model/check-in-metric-min';

@Component({
  selector: 'app-check-in-form-metric',
  templateUrl: './check-in-form-metric.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class CheckInFormMetricComponent {
  @Input()
  keyResult!: KeyResultMetric;

  @Input()
  checkIn!: CheckInMin;

  @Input()
  dialogForm!: FormGroup;

  protected readonly formInputCheck = formInputCheck;

  constructor() {}

  generateUnitLabel(): string {
    switch (this.keyResult.unit.unitName) {
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

  getCheckInMetric(): CheckInMetricMin {
    return this.checkIn as CheckInMetricMin;
  }
}
