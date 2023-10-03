import { Component, Inject } from '@angular/core';
import { KeyResultMetric } from '../../../types/model/KeyResultMetric';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KeyResult } from '../../../types/model/KeyResult';
import { KeyResultOrdinal } from '../../../types/model/KeyResultOrdinal';
import { CheckInMin } from '../../../types/model/CheckInMin';

@Component({
  selector: 'app-check-in-form',
  templateUrl: './check-in-form.component.html',
  styleUrls: ['./check-in-form.component.scss'],
})
export class CheckInFormComponent {
  keyResult: KeyResult;
  checkIn: CheckInMin = { confidence: 5 } as CheckInMin;
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
    this.setCheckIn();
    this.setDefaultValues();
  }

  setDefaultValues() {
    if (this.data.checkIn != null) {
      this.dialogForm.controls.value.setValue(this.checkIn.value!.toString());
      this.dialogForm.controls.confidence.setValue(this.checkIn.confidence);
      this.dialogForm.controls.changeInfo.setValue(this.checkIn.changeInfo);
      this.dialogForm.controls.initiatives.setValue(this.checkIn.initiatives);
      return;
    }
    if (this.keyResult.lastCheckIn?.value != null) {
      this.dialogForm.controls.value.setValue(this.keyResult.lastCheckIn.value.toString());
    }
  }

  saveCheckIn() {
    this.dialogForm.controls.confidence.setValue(this.checkIn.confidence);
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

  setCheckIn() {
    if (this.data.checkIn != null) {
      this.checkIn = this.data.checkIn;
    } else if (this.keyResult.lastCheckIn != null) {
      this.checkIn = this.keyResult.lastCheckIn;
    }
  }
}
