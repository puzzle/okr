import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KeyResult } from '../../types/model/KeyResult';
import { FormGroup } from '@angular/forms';
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

  dialogForm = new FormGroup({});

  constructor(
    public dialogRef: MatDialogRef<CheckInFormMetricComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.keyResult = data.keyResult;
    this.currentDate = new Date();
  }

  ngOnInit(): void {}

  isTouchedOrDirty(name: string) {
    return this.dialogForm.get(name)?.dirty || this.dialogForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.dialogForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  protected readonly errorMessages: any = errorMessages;
}
