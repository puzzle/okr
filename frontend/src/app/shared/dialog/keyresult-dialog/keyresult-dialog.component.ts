import { Component, Inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from '../../types/model/User';
import { Action } from '../../types/model/Action';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Objective } from '../../types/model/Objective';
import { KeyResult } from '../../types/model/KeyResult';
import { KeyResultMetricDTO } from '../../types/DTOs/KeyResultMetricDTO';
import { KeyResultOrdinalDTO } from '../../types/DTOs/KeyResultOrdinalDTO';
import { CloseState } from '../../types/enums/CloseState';
import { KeyresultService } from '../../services/keyresult.service';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { CONFIRM_DIALOG_WIDTH } from '../../constantLibary';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-keyresult-dialog',
  templateUrl: './keyresult-dialog.component.html',
  styleUrls: ['./keyresult-dialog.component.scss'],
})
export class KeyresultDialogComponent {
  actionList$: BehaviorSubject<Action[] | null> = new BehaviorSubject<Action[] | null>([] as Action[]);

  keyResultForm = new FormGroup({
    title: new FormControl<string>('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    owner: new FormControl<User | string | null>(null, [Validators.required, Validators.nullValidator]),
    actionList: new FormControl<Action[]>([]),
    unit: new FormControl<string | null>(null),
    baseline: new FormControl<number | null>(null),
    stretchGoal: new FormControl<number | null>(null),
    commitZone: new FormControl<string | null>(null),
    targetZone: new FormControl<string | null>(null),
    stretchZone: new FormControl<string | null>(null),
    keyResultType: new FormControl<string>('metric'),
  });

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { objective: Objective; keyResult: KeyResult },
    private keyResultService: KeyresultService,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<KeyresultDialogComponent>,
  ) {}

  isMetricKeyResult() {
    return this.keyResultForm.controls['keyResultType'].value === 'metric';
  }

  saveKeyResult(openNewDialog = false) {
    const value = this.keyResultForm.value;
    let keyResult = this.isMetricKeyResult()
      ? ({ ...value, objective: this.data.objective } as KeyResultMetricDTO)
      : ({ ...value, objective: this.data.objective, id: this.data.keyResult?.id } as KeyResultOrdinalDTO);
    keyResult.id = this.data.keyResult?.id;
    keyResult.version = this.data.keyResult?.version;
    keyResult.actionList = this.actionList$.getValue()!.filter((action: Action) => action.action !== '');
    this.keyResultService.saveKeyResult(keyResult).subscribe((returnValue) => {
      this.dialogRef.close({
        id: keyResult.id,
        version: keyResult.version,
        closeState: CloseState.SAVED,
        openNew: openNewDialog,
      });
    });
  }

  deleteKeyResult() {
    this.dialog
      .open(ConfirmDialogComponent, {
        data: {
          title: 'Key Result',
        },
        width: CONFIRM_DIALOG_WIDTH,
        height: 'auto',
      })
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

  isTouchedOrDirty(name: string) {
    return this.keyResultForm.get(name)?.dirty || this.keyResultForm.get(name)?.touched;
  }

  invalidOwner(): boolean {
    return (
      !!this.isTouchedOrDirty('owner') &&
      (typeof this.keyResultForm.value.owner === 'string' || !this.keyResultForm.value.owner)
    );
  }
}
