import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { KeyResultMetric } from '../../../shared/types/model/key-result-metric';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KeyResult } from '../../../shared/types/model/key-result';
import { KeyResultOrdinal } from '../../../shared/types/model/key-result-ordinal';
import { CheckInMin } from '../../../shared/types/model/check-in-min';
import { CheckInService } from '../../../services/check-in.service';
import { Action } from '../../../shared/types/model/action';
import { ActionService } from '../../../services/action.service';
import { formInputCheck, hasFormFieldErrors } from '../../../shared/common';
import { TranslateService } from '@ngx-translate/core';
import { CheckInMetricMin } from '../../../shared/types/model/check-in-metric-min';
import { CheckInOrdinalMin } from '../../../shared/types/model/check-in-ordinal-min';
import { BehaviorSubject } from 'rxjs';
import { Zone } from '../../../shared/types/enums/zone';
import { numberValidator } from '../../../shared/constant-library';

@Component({
  selector: 'app-check-in-form',
  templateUrl: './check-in-form.component.html',
  styleUrls: ['./check-in-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class CheckInFormComponent implements OnInit {
  keyResult: KeyResult;

  checkIn!: CheckInMin;

  currentDate: Date;

  actionList$: BehaviorSubject<Action[] | null> = new BehaviorSubject<Action[] | null>([] as Action[]);

  isAddingAction = false;

  dialogForm = new FormGroup({
    metricValue: new FormControl<number | undefined>(undefined, [Validators.required,
      numberValidator()]),
    ordinalZone: new FormControl<Zone>(Zone.FAIL, [Validators.required]),
    confidence: new FormControl<number>(5, [Validators.required,
      Validators.min(0),
      Validators.max(10)]),
    changeInfo: new FormControl<string>('', [Validators.maxLength(4096)]),
    initiatives: new FormControl<string>('', [Validators.maxLength(4096)]),
    actionList: new FormControl<Action[]>([])
  });

  checkInTypes: string[] = ['metricValue',
    'ordinalZone'];

  protected readonly formInputCheck = formInputCheck;

  protected readonly hasFormFieldErrors = hasFormFieldErrors;

  constructor(
    public dialogRef: MatDialogRef<CheckInFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private checkInService: CheckInService,
    private actionService: ActionService,
    private translate: TranslateService
  ) {
    this.currentDate = new Date();
    this.keyResult = data.keyResult;
    this.setDefaultValues();
    this.setValidators(this.keyResult.keyResultType);
  }

  ngOnInit() {
    const actionList = this.keyResult.actionList;
    this.actionList$.next(actionList);
    this.dialogForm.patchValue({ actionList: actionList });
  }

  setDefaultValues() {
    this.dialogForm.controls.actionList.setValue(this.keyResult.actionList);
    this.checkIn = this.data.checkIn;

    if (this.checkIn != null) {
      this.dialogForm.patchValue({ ...this.checkIn });
      this.setValueOrZone();
      return;
    }

    /* If KeyResult has lastCheckIn set checkIn to this value */
    if ((this.keyResult as KeyResultMetric | KeyResultOrdinal).lastCheckIn != null) {
      this.checkIn = {
        ...(this.keyResult as KeyResultMetric | KeyResultOrdinal).lastCheckIn,
        id: undefined
      } as CheckInMin;
      this.dialogForm.controls.confidence.setValue(this.checkIn.confidence);
      return;
    }

    /* If Check-in is null set as object with confidence 5 default value */
    this.checkIn = { confidence: 5 } as CheckInMin;
  }

  setValueOrZone() {
    this.keyResult.keyResultType === 'metric'
      ? this.dialogForm.controls.metricValue.setValue((this.checkIn as CheckInMetricMin).value)
      : this.dialogForm.controls.ordinalZone.setValue((this.checkIn as CheckInOrdinalMin).zone);
  }

  calculateTarget(keyResult: KeyResultMetric): number {
    return keyResult.stretchGoal - (keyResult.stretchGoal - keyResult.baseline) * 0.3;
  }

  saveCheckIn() {
    this.dialogForm.controls.confidence.setValue(this.checkIn.confidence);

    const actionList: Action[] = this.actionList$.value as Action[];
    this.dialogForm.patchValue({ actionList: actionList });
    const baseCheckIn: any = {
      id: this.checkIn.id,
      version: this.checkIn.version,
      keyResultId: this.keyResult.id,
      ...this.dialogForm.value
    };

    baseCheckIn.value = this.dialogForm.controls.metricValue.value;
    baseCheckIn.zone = this.dialogForm.controls.ordinalZone.value;

    this.checkInService.saveCheckIn(baseCheckIn)
      .subscribe(() => {
        this.actionService.updateActions(this.dialogForm.value.actionList!)
          .subscribe(() => {
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

  getActions(): Action[] {
    return this.dialogForm.controls['actionList'].value || [];
  }

  changeIsChecked(event: any, index: number) {
    const actions = this.dialogForm.value.actionList!;
    actions[index] = {
      ...actions[index],
      isChecked: event.checked
    };
    this.dialogForm.patchValue({ actionList: actions });
  }

  getDialogTitle(): string {
    return this.checkIn.id ? 'Check-in bearbeiten' : 'Check-in erfassen';
  }

  openActionEdit() {
    const actionList: Action[] = this.actionList$.value as Action[];
    actionList.push({
      action: '',
      priority: actionList.length,
      keyResultId: this.keyResult.id
    } as Action);
    this.actionList$.next(actionList);
    this.isAddingAction = true;
  }

  closeActionEdit() {
    let actionList: Action[] = this.actionList$.value as Action[];
    actionList = actionList.filter((action) => action.action.trim() != '');
    this.actionList$.next(actionList);
    this.dialogForm.patchValue({ actionList: actionList });
    this.isAddingAction = false;
  }

  setValidators(type: string) {
    this.checkInTypes.map((e) => this.dialogForm.get(e))
      .forEach((e) => e?.disable({ emitEvent: false }));
    this.dialogForm.get(this.checkInTypes.filter((formName) => formName.includes(type)))
      ?.enable({ emitEvent: false });
  }
}
