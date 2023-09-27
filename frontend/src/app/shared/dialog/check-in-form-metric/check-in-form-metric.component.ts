import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import errorMessages from '../../../../assets/errors/error-messages.json';
import { KeyResultMetric } from '../../types/model/KeyResultMetric';

@Component({
  selector: 'app-check-in-form-metric',
  templateUrl: './check-in-form-metric.component.html',
  styleUrls: ['./check-in-form-metric.component.scss'],
})
export class CheckInFormMetricComponent implements OnInit {
  keyResult: KeyResultMetric;
  currentDate: Date;

  dialogForm = new FormGroup({
    value: new FormControl<number>(0, [Validators.required]),
    confidence: new FormControl<number>(5, [Validators.required, Validators.min(1), Validators.max(10)]),
    changeInfo: new FormControl<string>('', [Validators.maxLength(4096)]),
    initiatives: new FormControl<string>('', [Validators.maxLength(4096)]),
  });

  constructor(
    public dialogRef: MatDialogRef<CheckInFormMetricComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.currentDate = new Date();
    this.keyResult = data.keyResult;
    this.setFormValues();
  }

  ngOnInit(): void {}

  setFormValues() {
    if (this.keyResult.lastCheckIn != null) {
      this.dialogForm.controls.value.setValue(+this.keyResult.lastCheckIn.value);
      this.dialogForm.controls.confidence.setValue(this.keyResult.lastCheckIn.confidence);
    }
  }

  saveCheckIn() {
    this.dialogForm.controls.confidence.setValue(this.keyResult.lastCheckIn!.confidence);
    console.log(this.keyResult.id);
    console.log(this.dialogForm.value);
  }

  isTouchedOrDirty(name: string) {
    return this.dialogForm.get(name)?.dirty || this.dialogForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.dialogForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  protected readonly errorMessages: any = errorMessages;
}
