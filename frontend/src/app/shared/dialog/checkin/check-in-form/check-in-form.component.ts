import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { KeyResultMetric } from '../../../types/model/KeyResultMetric';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KeyResult } from '../../../types/model/KeyResult';
import { KeyResultOrdinal } from '../../../types/model/KeyResultOrdinal';
import { CheckInMin } from '../../../types/model/CheckInMin';
import { ParseUnitValuePipe } from '../../../pipes/parse-unit-value/parse-unit-value.pipe';
import { CheckInService } from '../../../services/check-in.service';
import { Action } from '../../../types/model/Action';
import { ActionService } from '../../../services/action.service';
import { formInputCheck } from '../../../common';
import errorMessages from '../../../../../assets/errors/error-messages.json';

@Component({
  selector: 'app-check-in-form',
  templateUrl: './check-in-form.component.html',
  styleUrls: ['./check-in-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CheckInFormComponent implements OnInit {
  keyResult: KeyResult;
  checkIn!: CheckInMin;
  currentDate: Date;
  continued: boolean = false;
  dialogForm = new FormGroup({
    value: new FormControl<string>('', [Validators.required]),
    confidence: new FormControl<number>(5, [Validators.required, Validators.min(1), Validators.max(10)]),
    changeInfo: new FormControl<string>('', [Validators.maxLength(4096)]),
    initiatives: new FormControl<string>('', [Validators.maxLength(4096)]),
    actionList: new FormControl<Action[]>([]),
  });
  protected readonly formInputCheck = formInputCheck;
  protected readonly errorMessages: { [key: string]: string } = errorMessages;

  constructor(
    public dialogRef: MatDialogRef<CheckInFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public parserPipe: ParseUnitValuePipe,
    private checkInService: CheckInService,
    private actionService: ActionService,
  ) {
    this.currentDate = new Date();
    this.keyResult = data.keyResult;
    this.setDefaultValues();
  }

  ngOnInit() {
    this.dialogForm.patchValue({ actionList: this.keyResult.actionList });
  }

  isTouchedOrDirty(name: string) {
    return this.dialogForm.get(name)?.dirty || this.dialogForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string): string[] {
    const errors = this.dialogForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  setDefaultValues() {
    this.dialogForm.controls.actionList.setValue(this.keyResult.actionList);
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
      this.dialogForm.controls.confidence.setValue(this.checkIn.confidence);
      return;
    }
    /* If Check-in is null set as object with confidence 5 default value */
    this.checkIn = { confidence: 5 } as CheckInMin;
  }

  calculateTarget(keyResult: KeyResultMetric): number {
    return keyResult.stretchGoal - (keyResult.stretchGoal - keyResult.baseline) * 0.3;
  }

  saveCheckIn() {
    this.dialogForm.controls.confidence.setValue(this.checkIn.confidence);
    let checkIn: any = {
      ...this.dialogForm.value,
      id: this.checkIn.id,
      version: this.checkIn.version,
      keyResultId: this.keyResult.id,
    };
    if (this.keyResult.keyResultType === 'metric') {
      checkIn = {
        ...this.dialogForm.value,
        value: this.parserPipe.transform(this.dialogForm?.controls['value'].value!),
        keyResultId: this.keyResult.id,
        id: this.checkIn.id,
        version: this.checkIn.version,
      };
    }

    this.checkInService.saveCheckIn(checkIn).subscribe(() => {
      this.actionService.updateActions(this.dialogForm.value.actionList!).subscribe(() => {
        this.dialogRef.close();
      });
    });
  }

  getKeyResultMetric(): KeyResultMetric {
    return this.keyResult as KeyResultMetric;
  }

  getKeyResultOrdinal(): KeyResultOrdinal {
    return this.keyResult as KeyResultOrdinal;
  }

  getActions(): Action[] | null {
    return this.dialogForm.controls['actionList'].value;
  }

  changeIsChecked(event: any, index: number) {
    const actions = this.dialogForm.value.actionList!;
    actions[index] = { ...actions[index], isChecked: event.checked };
    this.dialogForm.patchValue({ actionList: actions });
  }
}
