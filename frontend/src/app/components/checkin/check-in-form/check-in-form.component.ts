import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { KeyResultMetric } from '../../../shared/types/model/KeyResultMetric';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KeyResult } from '../../../shared/types/model/KeyResult';
import { KeyResultOrdinal } from '../../../shared/types/model/KeyResultOrdinal';
import { CheckInMin } from '../../../shared/types/model/CheckInMin';
import { CheckInService } from '../../../services/check-in.service';
import { Action } from '../../../shared/types/model/Action';
import { ActionService } from '../../../services/action.service';
import { formInputCheck, hasFormFieldErrors } from '../../../shared/common';
import { TranslateService } from '@ngx-translate/core';
import { CheckIn } from '../../../shared/types/model/CheckIn';
import { CheckInMetricMin } from '../../../shared/types/model/CheckInMetricMin';
import { CheckInOrdinalMin } from '../../../shared/types/model/CheckInOrdinalMin';

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
    confidence: new FormControl<number>(5, [Validators.required, Validators.min(0), Validators.max(10)]),
    changeInfo: new FormControl<string>('', [Validators.maxLength(4096)]),
    initiatives: new FormControl<string>('', [Validators.maxLength(4096)]),
    actionList: new FormControl<Action[]>([]),
  });
  protected readonly formInputCheck = formInputCheck;
  protected readonly hasFormFieldErrors = hasFormFieldErrors;

  constructor(
    public dialogRef: MatDialogRef<CheckInFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private checkInService: CheckInService,
    private actionService: ActionService,
    private translate: TranslateService,
  ) {
    this.currentDate = new Date();
    this.keyResult = data.keyResult;
    this.setDefaultValues();
  }

  ngOnInit() {
    this.dialogForm.patchValue({ actionList: this.keyResult.actionList });
  }

  getErrorMessage(error: string, field: string, maxLength: number): string {
    return field + this.translate.instant('DIALOG_ERRORS.' + error).format(maxLength);
  }

  setDefaultValues() {
    this.dialogForm.controls.actionList.setValue(this.keyResult.actionList);
    if (this.data.checkIn != null) {
      this.checkIn = this.data.checkIn;
      if ((this.checkIn as CheckInMetricMin).value != null) {
        this.dialogForm.controls.value.setValue(this.getCheckInMetric().value!.toString());
      } else {
        this.dialogForm.controls.value.setValue(this.getCheckInOrdinal().zone!);
      }
      this.dialogForm.controls.confidence.setValue(this.checkIn.confidence);
      this.dialogForm.controls.changeInfo.setValue(this.checkIn.changeInfo);
      this.dialogForm.controls.initiatives.setValue(this.checkIn.initiatives);
      return;
    }
    /* If KeyResult has lastCheckIn set checkIn to this value */
    if ((this.keyResult as KeyResultMetric | KeyResultOrdinal).lastCheckIn != null) {
      this.checkIn = {
        ...(this.keyResult as KeyResultMetric | KeyResultOrdinal).lastCheckIn,
        id: undefined,
      } as CheckInMin;
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
    const baseCheckIn: any = {
      id: this.checkIn.id,
      version: this.checkIn.version,
      keyResultId: this.keyResult.id,
      confidence: this.dialogForm.controls.confidence.value,
      changeInfo: this.dialogForm.controls.changeInfo.value,
      initiatives: this.dialogForm.controls.initiatives.value,
      actionList: this.dialogForm.controls.actionList.value,
    };
    const checkIn: CheckIn = {
      ...baseCheckIn,
      [this.keyResult.keyResultType === 'ordinal' ? 'zone' : 'value']: this.dialogForm.controls.value.value,
    };

    this.checkInService.saveCheckIn(checkIn).subscribe(() => {
      this.actionService.updateActions(this.dialogForm.value.actionList!).subscribe(() => {
        this.dialogRef.close();
      });
    });
  }

  getCheckInMetric(): CheckInMetricMin {
    return this.checkIn as CheckInMetricMin;
  }

  getCheckInOrdinal(): CheckInOrdinalMin {
    return this.checkIn as CheckInOrdinalMin;
  }

  getKeyResultMetric(): KeyResultMetric {
    return this.keyResult as KeyResultMetric;
  }

  getKeyResultOrdinal(): KeyResultOrdinal {
    return this.keyResult as KeyResultOrdinal;
  }

  getActions(): Action[] {
    return this.dialogForm.controls['actionList'].value || [];
  }

  changeIsChecked(event: any, index: number) {
    const actions = this.dialogForm.value.actionList!;
    actions[index] = { ...actions[index], isChecked: event.checked };
    this.dialogForm.patchValue({ actionList: actions });
  }

  getDialogTitle(): string {
    return this.checkIn.id ? 'Check-in bearbeiten' : 'Check-in erfassen';
  }
}
