import {ChangeDetectionStrategy, Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {User} from '../shared/types/model/User';
import {KeyResult} from '../shared/types/model/KeyResult';
import {KeyresultService} from '../shared/services/keyresult.service';
import {testUser} from '../shared/testData';
import {KeyResultMetricDTO} from '../shared/types/DTOs/KeyResultMetricDTO';
import errorMessages from '../../assets/errors/error-messages.json';
import {ConfirmDialogComponent} from '../shared/dialog/confirm-dialog/confirm-dialog.component';
import {Objective} from '../shared/types/model/Objective';
import {KeyResultEmitOrdinalDTO} from '../shared/types/DTOs/KeyResultEmitOrdinalDTO';
import {KeyResultEmitDTO} from '../shared/types/DTOs/KeyResultEmitDTO';
import {KeyResultEmitMetricDTO} from '../shared/types/DTOs/KeyResultEmitMetricDTO';
import {KeyResultDTO} from '../shared/types/DTOs/KeyResultDTO';
import {KeyResultOrdinalDTO} from '../shared/types/DTOs/KeyResultOrdinalDTO';
import {KeyResultMetric} from '../shared/types/model/KeyResultMetric';
import {KeyResultOrdinal} from '../shared/types/model/KeyResultOrdinal';

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
  protected readonly errorMessages: any = errorMessages;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { objective: Objective; keyResult: KeyResult },
    public dialogRef: MatDialogRef<KeyResultDialogComponent>,
    private keyResultService: KeyresultService,
    public dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    if (this.data.keyResult) {
      this.isMetricKeyResult = this.data.keyResult.keyResultType == 'metric';
      if (this.isMetricKeyResult) {
        let keyResultMetric = this.data.keyResult as KeyResultMetric;
        this.keyResultForm.setValue({
          title: keyResultMetric.title,
          description: keyResultMetric.description,
          owner: keyResultMetric.owner,
          unit: keyResultMetric.unit,
          baseline: keyResultMetric.baseline,
          stretchGoal: keyResultMetric.stretchGoal,
          commitZone: null,
          targetZone: null,
          stretchZone: null,
        });
      } else {
        let keyResultOrdinal = this.data.keyResult as KeyResultOrdinal;
        this.keyResultForm.setValue({
          title: keyResultOrdinal.title,
          description: keyResultOrdinal.description,
          owner: keyResultOrdinal.owner,
          unit: null,
          baseline: null,
          stretchGoal: null,
          commitZone: keyResultOrdinal.commitZone,
          targetZone: keyResultOrdinal.targetZone,
          stretchZone: keyResultOrdinal.stretchZone,
        });
      }
    }
  }

  saveKeyResult(openNewDialog = false) {
    const value = this.keyResultForm.value;
    let keyResult: KeyResultDTO;
    if (this.isMetricKeyResult) {
      keyResult = {
        id: this.data.keyResult ? this.data.keyResult.id : null,
        title: value.title,
        description: value.description,
        objective: this.data.objective ? this.data.objective : this.data.keyResult.objective,
        keyResultType: 'metric',
        owner: value.owner,
        unit: value.unit,
        baseline: value.baseline,
        stretchGoal: value.stretchGoal,
      } as unknown as KeyResultMetricDTO;
    } else {
      keyResult = {
        id: this.data.keyResult ? this.data.keyResult.id : null,
        title: value.title,
        description: value.description,
        objective: this.data.objective ? this.data.objective : this.data.keyResult.objective,
        keyResultType: 'ordinal',
        owner: value.owner,
        commitZone: value.commitZone,
        targetZone: value.targetZone,
        stretchZone: value.stretchZone,
      } as unknown as KeyResultOrdinalDTO;
    }

    if (this.data.objective) {
      keyResult.objective = this.data.objective;
    }
    this.keyResultService.saveKeyResult(keyResult).subscribe((returnValue) => {
      this.dialogRef.close({
        id: keyResult.id,
        closeState: CloseState.SAVED,
        openNew: openNewDialog,
      });
    });
  }

  openNew() {
    this.saveKeyResult(true);
  }

  deleteKeyResult() {
    this.dialog
      .open(ConfirmDialogComponent, {
        data: {
          title: 'Key Result',
        },
        width: '15em',
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

  isTouchedOrDirty(name: string) {
    return this.keyResultForm.get(name)?.dirty || this.keyResultForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.keyResultForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  saveEmittedData(keyResult: KeyResultEmitDTO) {
    this.isMetricKeyResult = keyResult.keyresultType == 'metric';
    if (keyResult.keyresultType == 'metric') {
      let metricKeyResult = keyResult as KeyResultEmitMetricDTO;
      this.keyResultForm.patchValue({
        baseline: metricKeyResult.baseline,
        stretchGoal: metricKeyResult.stretchGoal,
        unit: metricKeyResult.unit,
      });
    } else {
      let ordinalKeyResult = keyResult as KeyResultEmitOrdinalDTO;
      this.keyResultForm.patchValue({
        commitZone: ordinalKeyResult.commitZone,
        targetZone: ordinalKeyResult.targetZone,
        stretchZone: ordinalKeyResult.stretchZone,
      });
    }
  }
}
