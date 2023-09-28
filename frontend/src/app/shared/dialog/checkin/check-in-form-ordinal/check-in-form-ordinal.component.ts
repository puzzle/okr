import { Component, Inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { KeyResultOrdinal } from '../../../types/model/KeyResultOrdinal';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-check-in-form-ordinal',
  templateUrl: './check-in-form-ordinal.component.html',
  styleUrls: ['./check-in-form-ordinal.component.scss'],
})
export class CheckInFormOrdinalComponent {
  keyResult: KeyResultOrdinal;
  currentDate: Date;
  continued: boolean = false;
  dialogForm = new FormGroup({
    value: new FormControl<string>('', [Validators.required]),
    confidence: new FormControl<number>(5, [Validators.required, Validators.min(1), Validators.max(10)]),
    changeInfo: new FormControl<string>('', [Validators.maxLength(4096)]),
    initiatives: new FormControl<string>('', [Validators.maxLength(4096)]),
  });

  constructor(
    public dialogRef: MatDialogRef<CheckInFormOrdinalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.keyResult = data.keyResult;
    this.currentDate = new Date();
  }

  saveCheckIn() {
    this.dialogForm.controls.confidence.setValue(this.keyResult.lastCheckIn!.confidence);
    let checkIn = { ...this.dialogForm.value, keyResultId: this.keyResult.id };
    this.dialogRef.close({ data: checkIn });
  }
}
