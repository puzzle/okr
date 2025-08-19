import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Objective } from '../../shared/types/model/objective';
import { KeyResult } from '../../shared/types/model/key-result';
import { KeyResultMetricDto } from '../../shared/types/DTOs/key-result-metric-dto';
import { KeyResultOrdinalDto } from '../../shared/types/DTOs/key-result-ordinal-dto';
import { CloseState } from '../../shared/types/enums/close-state';
import { KeyResultService } from '../../services/key-result.service';
import { DialogService } from '../../services/dialog.service';
import { getKeyResultForm } from '../../shared/constant-library';
import { UserService } from '../../services/user.service';
import { KeyResultDto } from '../../shared/types/DTOs/key-result-dto';
import { itemListToActionList, trackDeletedItems } from '../../shared/common';
import { Observable } from 'rxjs';
import { ActionService } from '../../services/action.service';


@Component({
  selector: 'app-key-result-dialog',
  templateUrl: './key-result-dialog.component.html',
  standalone: false
})
export class KeyResultDialogComponent implements OnInit {
  keyResultForm: FormGroup = getKeyResultForm();

  keyResultTypes: string[] = ['metric',
    'ordinal'];

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: {
      objective: Objective;
      keyResult: KeyResult;
    },
    private keyResultService: KeyResultService,
    public dialogService: DialogService,
    public dialogRef: MatDialogRef<KeyResultDialogComponent>,
    private userService: UserService,
    private actionService: ActionService
  ) {
  }

  deletedItems: Observable<any> = (this.keyResultForm.get('actionList') as FormArray).valueChanges.pipe(trackDeletedItems());

  isMetricKeyResult() {
    return this.keyResultForm.controls['keyResultType'].value === 'metric';
  }

  saveKeyResult(openNewDialog = false) {
    const roundToTwoDecimals = (num: number) => parseFloat(num.toFixed(2));
    this.deletedItems.subscribe((e: any[]) => e.forEach((elem: any) => this.actionService.deleteAction(elem.id)
      .subscribe()));
    this.keyResultForm.controls['metric'].enable();
    this.keyResultForm.controls['ordinal'].enable();
    let keyResult: KeyResultDto = this.keyResultForm.value;
    const values = this.keyResultForm.get('metric')?.value;
    const num: number = (values.stretchGoal - values.baseline) * 0.3 + values.baseline;
    keyResult.actionList = itemListToActionList(this.keyResultForm.getRawValue().actionList, this.data.keyResult?.id || null);
    const commit = { commitGoal: roundToTwoDecimals(num) };
    if (this.isMetricKeyResult()) {
      keyResult = { ...keyResult,
        ...this.keyResultForm.get('metric')?.value,
        ...commit } as KeyResultMetricDto;
    } else {
      keyResult = { ...keyResult,
        ...this.keyResultForm.get('ordinal')?.value } as KeyResultOrdinalDto;
    }
    keyResult.objective = this.data.objective;
    keyResult.id = this.data.keyResult?.id;
    keyResult.version = this.data.keyResult?.version;

    this.keyResultService.saveKeyResult(keyResult)
      .subscribe((returnValue) => {
        this.dialogRef.close({
          id: returnValue.id,
          version: returnValue.version,
          closeState: CloseState.SAVED,
          openNew: openNewDialog
        });
      });
  }

  deleteKeyResult() {
    this.dialogService
      .openConfirmDialog('CONFIRMATION.DELETE.KEY_RESULT')
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.keyResultService
            .deleteKeyResult(this.data.keyResult.id)
            .subscribe(() => this.dialogRef.close({ closeState: CloseState.DELETED }));
        }
      });
  }

  openNew() {
    this.saveKeyResult(true);
  }

  getDialogTitle(): string {
    return this.data.keyResult ? 'Key Result bearbeiten' : 'Key Result erfassen';
  }

  setValidators(type: string) {
    this.keyResultTypes.map((e) => this.keyResultForm.get(e))
      .forEach((e) => e?.disable({ emitEvent: false }));
    this.keyResultForm.get(type)
      ?.enable({ emitEvent: false });
  }

  ngOnInit(): void {
    this.setValidators(this.keyResultForm.get('keyResultType')?.value);
    this.keyResultForm.get('owner')
      ?.setValue(this.userService.getCurrentUser());
    this.keyResultForm.get('keyResultType')?.valueChanges.subscribe((value) => {
      this.setValidators(value);
    });
    this.deletedItems.subscribe();
  }
}
