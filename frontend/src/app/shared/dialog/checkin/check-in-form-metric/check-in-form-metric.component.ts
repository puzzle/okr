import { AfterViewInit, ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import errorMessages from '../../../../../assets/errors/error-messages.json';
import { KeyResultMetric } from '../../../types/model/KeyResultMetric';
import { UnitValueTransformationPipe } from '../../../pipes/unit-value-transformation/unit-value-transformation.pipe';
import { CheckInMin } from '../../../types/model/CheckInMin';

@Component({
  selector: 'app-check-in-form-metric',
  templateUrl: './check-in-form-metric.component.html',
  styleUrls: ['./check-in-form-metric.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CheckInFormMetricComponent implements AfterViewInit {
  @Input()
  keyResult!: KeyResultMetric;
  @Input()
  checkIn!: CheckInMin;
  @Input()
  dialogForm!: FormGroup;
  protected readonly errorMessages: any = errorMessages;

  constructor(private pipe: UnitValueTransformationPipe) {}

  formatValue() {
    this.dialogForm?.controls['value'].setValue(this.pipe.transform(this.parseValue(), this.keyResult.unit));
  }

  parseValue(): number {
    return +this.dialogForm?.controls['value'].value?.replaceAll('%', '').replaceAll('.-', '')!;
  }

  isTouchedOrDirty(name: string) {
    return this.dialogForm.get(name)?.dirty || this.dialogForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.dialogForm.get(name)?.errors;
    return errors === null ? [] : Object.keys(errors!);
  }

  ngAfterViewInit(): void {
    this.formatValue();
  }
}
