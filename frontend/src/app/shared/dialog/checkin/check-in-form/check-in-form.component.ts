import { Component, Inject } from '@angular/core';
import { KeyResultMetric } from '../../../types/model/KeyResultMetric';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KeyResult } from '../../../types/model/KeyResult';
import { KeyResultOrdinal } from '../../../types/model/KeyResultOrdinal';

@Component({
  selector: 'app-check-in-form',
  templateUrl: './check-in-form.component.html',
  styleUrls: ['./check-in-form.component.scss'],
})
export class CheckInFormComponent {
  keyResult: KeyResult;
  currentDate: Date;
  continued: boolean = false;

  dialogForm = new FormGroup({
    value: new FormControl<string>('', [Validators.required]),
    confidence: new FormControl<number>(5, [Validators.required, Validators.min(1), Validators.max(10)]),
    changeInfo: new FormControl<string>('', [Validators.maxLength(4096)]),
    initiatives: new FormControl<string>('', [Validators.maxLength(4096)]),
  });

  constructor(
    public dialogRef: MatDialogRef<CheckInFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.currentDate = new Date();
    this.keyResult = data.keyResult;
    this.setDefaultValues();
  }

  setDefaultValues() {
    if (this.keyResult.lastCheckIn?.value != null) {
      this.dialogForm.controls.value.setValue(this.keyResult.lastCheckIn.value.toString());
      this.dialogForm.controls.confidence.setValue(this.keyResult.lastCheckIn.confidence);
    }
  }

  saveCheckIn() {
    this.dialogForm.controls.confidence.setValue(this.keyResult.lastCheckIn!.confidence);
    let checkIn: any = { ...this.dialogForm.value, keyResultId: this.keyResult.id };
    if (this.keyResult.keyResultType === 'metric') {
      checkIn = { ...this.dialogForm.value, value: this.parseValue(), keyResultId: this.keyResult.id };
    }
    this.dialogRef.close({ data: checkIn });
  }

  parseValue(): number {
    return +this.dialogForm?.controls['value'].value?.replaceAll('%', '').replaceAll('.-', '')!;
  }

  getKeyResultMetric(): KeyResultMetric {
    return this.keyResult as KeyResultMetric;
  }

  getKeyResultOrdinal(): KeyResultOrdinal {
    return this.keyResult as KeyResultOrdinal;
  }
}
