import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { Action } from '../../shared/types/model/action';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Objective } from '../../shared/types/model/objective';
import { KeyResult } from '../../shared/types/model/key-result';
import { KeyResultMetricDto } from '../../shared/types/DTOs/key-result-metric-dto';
import { KeyResultOrdinalDto } from '../../shared/types/DTOs/key-result-ordinal-dto';
import { CloseState } from '../../shared/types/enums/close-state';
import { KeyResultService } from '../../services/key-result.service';
import { DialogService } from '../../services/dialog.service';
import { Unit } from '../../shared/types/enums/unit';
import { BehaviorSubject } from 'rxjs';


@Component({
  selector: 'app-key-result-dialog',
  templateUrl: './key-result-dialog.component.html',
  standalone: false
})
export class KeyResultDialogComponent {
  isMetric = new BehaviorSubject<boolean>(true);

  keyResultForm: FormGroup;
  /*
   *     = new FormGroup({
   *   title: new FormControl<string>('', [Validators.required,
   *     Validators.minLength(2),
   *     Validators.maxLength(250)]),
   *   description: new FormControl<string>('', [Validators.maxLength(4096)]),
   *   owner: new FormControl<User | string | null>(null, [Validators.required,
   *     Validators.nullValidator]),
   *   actionList: new FormControl<Action[]>([]),
   *   unit: new FormControl<Unit>(Unit.NUMBER),
   *   baseline: new FormControl<number>(0, this.getValidatorsForKeyResultMetric(this.isMetric)),
   *   targetGoal: new FormControl<number>(0),
   *   stretchGoal: new FormControl<number>(0),
   *   commitZone: new FormControl<string>(""),
   *   targetZone: new FormControl<string>(""),
   *   stretchZone: new FormControl<string>(""),
   *   keyResultType: new FormControl<string>('metric')
   * });
   */

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { objective: Objective;
      keyResult: KeyResult; },
    private keyResultService: KeyResultService,
    public dialogService: DialogService,
    public dialogRef: MatDialogRef<KeyResultDialogComponent>,
    private fb: FormBuilder
  ) {
    this.keyResultForm = this.fb.group({
      // general: this.fb.group({
      title: ['',
        [Validators.required,
          Validators.minLength(2),
          Validators.maxLength(250)]],
      description: ['',
        [Validators.maxLength(4096)]],
      owner: [null,
        [Validators.required,
          Validators.nullValidator]],
      actionList: [[]],
      keyResultType: ['metric'],
      // }),
      metric: this.fb.group({
        unit: [Unit.NUMBER],
        baseline: [0,
          this.getValidatorsForKeyResultMetric(true)],
        targetGoal: [0],
        stretchGoal: [0]
      }),
      ordinal: this.fb.group({
        commitZone: [''],
        targetZone: [''],
        stretchZone: ['']
      })
    });
  }

  isMetricKeyResult() {
    return this.keyResultForm.controls['keyResultType'].value === 'metric';
  }

  updateValidators() {

    // this.keyResultForm.controls['metric']?.forEach((control: FormControl) => control.setValidators(this.getValidatorsForKeyResultMetric(this.isMetric.value));
  }

  saveKeyResult(openNewDialog = false) {
    const value = this.keyResultForm.value;
    const keyResult = this.isMetricKeyResult()
      ? ({ ...value,
        objective: this.data.objective } as KeyResultMetricDto)
      : ({ ...value,
        objective: this.data.objective,
        id: this.data.keyResult?.id } as KeyResultOrdinalDto);
    keyResult.id = this.data.keyResult?.id;
    keyResult.version = this.data.keyResult?.version!;
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

  isTouchedOrDirty(name: string) {
    return this.keyResultForm.get(name)?.dirty || this.keyResultForm.get(name)?.touched;
  }

  invalidOwner(): boolean {
    return (
      !!this.isTouchedOrDirty('owner') &&
      (typeof this.keyResultForm.value.owner === 'string' || !this.keyResultForm.value.owner)
    );
  }

  getDialogTitle(): string {
    return this.data.keyResult ? 'Key Result bearbeiten' : 'Key Result erfassen';
  }

  getValidatorsForKeyResultMetric(isMetric: boolean): ValidatorFn[] {
    return isMetric
      ? [Validators.required,
        Validators.maxLength(400)]
      : [];
  }

  toggle() {
    if (this.keyResultForm.get('metric')?.disabled) {
      this.keyResultForm.get('metric')
        ?.enable();
    } else {
      this.keyResultForm.get('metric')
        ?.disable();
    }
  }
}
