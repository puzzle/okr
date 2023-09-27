import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import errorMessages from '../../../../assets/errors/error-messages.json';
import { KeyResultMetric } from '../../types/model/KeyResultMetric';
import { UnitTransformationPipe } from '../../pipes/unit-transformation.pipe';

@Component({
  selector: 'app-check-in-form-metric',
  templateUrl: './check-in-form-metric.component.html',
  styleUrls: ['./check-in-form-metric.component.scss'],
})
export class CheckInFormMetricComponent implements OnInit {
  keyResult: KeyResultMetric;
  currentDate: Date;

  dialogForm = new FormGroup({
    value: new FormControl<string>('', [Validators.required]),
    confidence: new FormControl<number>(5, [Validators.required, Validators.min(1), Validators.max(10)]),
    changeInfo: new FormControl<string>('', [Validators.maxLength(4096)]),
    initiatives: new FormControl<string>('', [Validators.maxLength(4096)]),
  });
  protected readonly errorMessages: any = errorMessages;

  constructor(
    public dialogRef: MatDialogRef<CheckInFormMetricComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private pipe: UnitTransformationPipe,
  ) {
    this.currentDate = new Date();
    this.keyResult = data.keyResult;
    this.setDefaultValues();
    this.formatValue();
  }

  ngOnInit(): void {
    console.log(this.keyResult);
  }

  setDefaultValues() {
    if (this.keyResult.lastCheckIn?.value != null) {
      this.dialogForm.controls.value.setValue(this.keyResult.lastCheckIn.value.toString());
      this.dialogForm.controls.confidence.setValue(this.keyResult.lastCheckIn.confidence);
    }
  }

  formatValue() {
    this.dialogForm.controls.value.setValue(this.pipe.transform(this.parseValue(), this.keyResult.unit));
  }

  parseValue(): number {
    return +this.dialogForm.controls.value.value?.replaceAll('%', '').replaceAll('.-', '')!;
  }

  saveCheckIn() {
    this.dialogForm.controls.confidence.setValue(this.keyResult.lastCheckIn!.confidence);
    let checkIn = { ...this.dialogForm.value, value: this.parseValue() };
    console.log(checkIn);
  }

  isTouchedOrDirty(name: string) {
    return this.dialogForm.get(name)?.dirty || this.dialogForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.dialogForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }
}
