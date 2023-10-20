import { ChangeDetectionStrategy, Component, Inject } from '@angular/core';
import { KeyResultMetric } from '../../../types/model/KeyResultMetric';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KeyResult } from '../../../types/model/KeyResult';
import { KeyResultOrdinal } from '../../../types/model/KeyResultOrdinal';
import { CheckInMin } from '../../../types/model/CheckInMin';
import { ParseUnitValuePipe } from '../../../pipes/parse-unit-value/parse-unit-value.pipe';
import { CheckInService } from '../../../services/check-in.service';
import { DATE_FORMAT } from '../../../constantLibary';

@Component({
  selector: 'app-check-in-form',
  templateUrl: './check-in-form.component.html',
  styleUrls: ['./check-in-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CheckInFormComponent {
  keyResult: KeyResult;
  checkIn!: CheckInMin;
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
    public parserPipe: ParseUnitValuePipe,
    private checkInService: CheckInService,
  ) {
    this.currentDate = new Date();
    this.keyResult = data.keyResult;
    this.setDefaultValues();
  }

  setDefaultValues() {
    if (this.data.checkIn != null) {
      this.checkIn = this.data.checkIn;
      this.dialogForm.controls.value.setValue(this.checkIn.value!.toString());
      this.dialogForm.controls.confidence.setValue(this.checkIn.confidence);
      this.dialogForm.controls.changeInfo.setValue(this.checkIn.changeInfo);
      this.dialogForm.controls.initiatives.setValue(this.checkIn.initiatives);
      return;
    }
    /* If KeyResult has lastCheckIn set checkIn to this value */
    if (this.keyResult.lastCheckIn != null) {
      this.checkIn = { ...this.keyResult.lastCheckIn, id: undefined };
      this.dialogForm.controls.value.setValue(this.checkIn.value!.toString());
      this.dialogForm.controls.confidence.setValue(this.checkIn.confidence);
      return;
    }
    /* If Check-in is null set as object with confidence 5 default value */
    this.checkIn = { confidence: 5 } as CheckInMin;
  }

  saveCheckIn() {
    this.dialogForm.controls.confidence.setValue(this.checkIn.confidence);
    let checkIn: any = { ...this.dialogForm.value, id: this.checkIn.id, keyResultId: this.keyResult.id };
    if (this.keyResult.keyResultType === 'metric') {
      checkIn = {
        ...this.dialogForm.value,
        value: this.parserPipe.transform(this.dialogForm?.controls['value'].value!),
        keyResultId: this.keyResult.id,
        id: this.checkIn.id,
      };
    }

    this.checkInService.saveCheckIn(checkIn).subscribe(() => {
      this.dialogRef.close();
    });
  }

  getKeyResultMetric(): KeyResultMetric {
    return this.keyResult as KeyResultMetric;
  }

  getKeyResultOrdinal(): KeyResultOrdinal {
    return this.keyResult as KeyResultOrdinal;
  }

  getStepLabel(): string {
    if (this.continued) {
      return '(2/2)';
    }
    return '(1/2)';
  }

  protected readonly DATE_FORMAT = DATE_FORMAT;
}
