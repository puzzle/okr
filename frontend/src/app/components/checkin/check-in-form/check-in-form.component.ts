import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { KeyResultMetric } from '../../../shared/types/model/key-result-metric';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KeyResult } from '../../../shared/types/model/key-result';
import { KeyResultOrdinal } from '../../../shared/types/model/key-result-ordinal';
import { CheckInMin } from '../../../shared/types/model/check-in-min';
import { CheckInService } from '../../../services/check-in.service';
import { Action } from '../../../shared/types/model/action';
import { ActionService } from '../../../services/action.service';
import {
  actionListToItemList,
  formInputCheck,
  itemListToActionList,
  trackDeletedItems
} from '../../../shared/common';
import { CheckInMetricMin } from '../../../shared/types/model/check-in-metric-min';
import { CheckInOrdinalMin } from '../../../shared/types/model/check-in-ordinal-min';
import { Zone } from '../../../shared/types/enums/zone';
import { numberValidator } from '../../../shared/constant-library';

import { FormControlsOf, Item } from '../../action-plan/action-plan.component';
import { Observable } from 'rxjs';
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
    actionList: new FormArray<FormGroup<FormControlsOf<Item>>>([])
  });

  checkInTypes: string[] = ['metricValue',
    'ordinalZone'];

  deletedItems: Observable<any> = this.getFormControlArray().valueChanges.pipe(trackDeletedItems());

  actionPlanAddItemSubject = new ReplaySubject<Item | undefined>();

  protected readonly formInputCheck = formInputCheck;

  constructor(
    public dialogRef: MatDialogRef<CheckInFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private checkInService: CheckInService,
    private actionService: ActionService
  ) {
    this.currentDate = new Date();
    this.keyResult = data.keyResult;
    this.setDefaultValues();
    this.setValidators(this.keyResult.keyResultType);
  }

  ngOnInit() {
    const actionList = this.keyResult.actionList;
    const items = actionListToItemList(actionList);
    this.dialogForm.patchValue({ actionList: items });
    this.deletedItems.subscribe();
  }

  setDefaultValues() {
    actionListToItemList(this.keyResult.actionList)
      .forEach((e: Item) => {
        this.addNewItem(e);
      });
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
    this.deletedItems.subscribe((e: any[]) => e.forEach((elem: any) => this.actionService.deleteAction(elem.id)
      .subscribe()));
    const actionList: Action[] = itemListToActionList(this.dialogForm.getRawValue().actionList, this.keyResult.id);
    const baseCheckIn: any = {
      id: this.checkIn.id,
      version: this.checkIn.version,
      keyResultId: this.keyResult.id,
      confidence: this.dialogForm.controls.confidence.value,
      changeInfo: this.dialogForm.controls.changeInfo.value,
      initiatives: this.dialogForm.controls.initiatives.value
    };

    if (this.keyResult.keyResultType === 'metric') {
      baseCheckIn.value = this.dialogForm.controls.metricValue.value;
    }
    if (this.keyResult.keyResultType === 'ordinal') {
      baseCheckIn.zone = this.dialogForm.controls.ordinalZone.value;
    }

    this.checkInService.saveCheckIn(baseCheckIn)
      .subscribe(() => {
        this.actionService.updateActions(actionList)
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

  getDialogTitle(): string {
    return this.checkIn.id ? 'Check-in bearbeiten' : 'Check-in erfassen';
  }

  openActionEdit() {
    this.actionPlanAddItemSubject.next(undefined);
    this.isAddingAction = true;
  }

  closeActionEdit() {
    this.isAddingAction = false;
  }

  getFormControlArray() {
    return this.dialogForm?.get('actionList') as FormArray<FormGroup<FormControlsOf<Item>>>;
  }

  addNewItem(item?: Item) {
    this.getFormControlArray()
      ?.push(this.initFormGroupFromItem(item));
  }

  setValidators(type: string) {
    this.checkInTypes.map((e) => this.dialogForm.get(e))
      .forEach((e) => e?.disable({ emitEvent: false }));
    this.dialogForm.get(this.checkInTypes.filter((formName) => formName.includes(type)))
      ?.enable({ emitEvent: false });
  }

  initFormGroupFromItem(item?: Item): FormGroup<FormControlsOf<Item>> {
    return new FormGroup({
      item: new FormControl<string>(item?.item || '', [Validators.minLength(2)]),
      id: new FormControl<number | undefined>(item?.id || undefined),
      isChecked: new FormControl<boolean>(item?.isChecked || false)
    } as FormControlsOf<Item>);
  }
}
