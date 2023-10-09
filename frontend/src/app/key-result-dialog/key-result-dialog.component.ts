import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from '../shared/types/model/User';
import { KeyResult } from '../shared/types/model/KeyResult';
import { KeyresultService } from '../shared/services/keyresult.service';
import { testUser } from '../shared/testData';
import { KeyResultMetricDTO } from '../shared/types/DTOs/KeyResultMetricDTO';
import errorMessages from '../../assets/errors/error-messages.json';
import { ConfirmDialogComponent } from '../shared/dialog/confirm-dialog/confirm-dialog.component';
import { Objective } from '../shared/types/model/Objective';

export enum CloseState {
  SAVED,
  DELETED,
  CANCELED,
  UNKNOWN,
}

@Component({
  selector: 'app-key-result-dialog',
  templateUrl: './key-result-dialog.component.html',
  styleUrls: ['./key-result-dialog.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyResultDialogComponent implements OnInit {
  isMetricKeyResult: boolean = true;
  keyResultForm = new FormGroup({
    title: new FormControl<string>('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    // TODO add owner from Dropdown and [Validators.required, Validators.nullValidator]
    owner: new FormControl<User | null>(testUser),
    // TODO remove the preset values when adding metric ordinal component
    unit: new FormControl<string | null>('CHF'),
    baseline: new FormControl<number | null>(3),
    stretchGoal: new FormControl<number | null>(25),
    commitZone: new FormControl<string | null>(null),
    targetZone: new FormControl<string | null>(null),
    stretchZone: new FormControl<string | null>(null),
  });

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { objective: Objective; keyResult: KeyResult },
    public dialogRef: MatDialogRef<KeyResultDialogComponent>,
    private keyResultService: KeyresultService,
    public dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    // TODO set isMetricKeyResult boolean if KeyResult is not metric
    if (this.data.keyResult) {
      // TODO set values for unit baseLine stretchGoal commit-, target-, stretchZone
      this.keyResultForm.setValue({
        title: this.data.keyResult.title,
        description: this.data.keyResult.description,
        owner: this.data.keyResult.owner,
        unit: 'CHF',
        baseline: 3,
        stretchGoal: 25,
        commitZone: null,
        targetZone: null,
        stretchZone: null,
      });
    }
  }

  saveKeyResult(openNewDialog = false) {
    const value = this.keyResultForm.value;
    let keyResult: KeyResultMetricDTO = {
      id: this.data.keyResult ? this.data.keyResult.id : null,
      title: value.title,
      description: value.description,
      objective: this.data.objective ? this.data.objective : this.data.keyResult.objective,
      keyResultType: this.isMetricKeyResult ? 'metric' : 'ordinal',
      owner: value.owner,
      unit: value.unit,
      baseline: value.baseline,
      stretchGoal: value.stretchGoal,
    } as unknown as KeyResultMetricDTO;
    if (this.data.objective) {
      keyResult.objective = this.data.objective;
    }
    this.keyResultService.saveKeyResult(keyResult).subscribe((returnValue) => {
      this.dialogRef.close({ closeState: CloseState.SAVED, id: keyResult.id });
    });
  }

  openNew() {
    this.saveKeyResult(true);
  }

  deleteKeyResult() {
    if (this.data.keyResult.lastCheckIn?.id == undefined) {
      //ToDo: Make ConfirmDialogComponent generic since its also used in other cases
      this.dialog
        .open(ConfirmDialogComponent, {
          width: '15em',
          height: 'auto',
        })
        .afterClosed()
        .subscribe((result) => {
          if (result == 'deleteKeyResult') {
            this.keyResultService.deleteKeyResult(this.data.keyResult.id).subscribe(() =>
              this.dialogRef.close({
                closeState: CloseState.DELETED,
                keyResult: this.data.keyResult,
                delete: true,
                openNew: false,
              }),
            );
          }
        });
    }
  }

  isTouchedOrDirty(name: string) {
    return this.keyResultForm.get(name)?.dirty || this.keyResultForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.keyResultForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  protected readonly errorMessages: any = errorMessages;
}
