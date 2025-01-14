import { Component, Inject } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Objective } from '../../shared/types/model/objective';
import { KeyResult } from '../../shared/types/model/key-result';
import { KeyResultMetricDto } from '../../shared/types/DTOs/key-result-metric-dto';
import { KeyResultOrdinalDto } from '../../shared/types/DTOs/key-result-ordinal-dto';
import { CloseState } from '../../shared/types/enums/close-state';
import { KeyResultService } from '../../services/key-result.service';
import { DialogService } from '../../services/dialog.service';
import { getKeyResultForm } from '../../shared/constant-library';


@Component({
  selector: 'app-key-result-dialog',
  templateUrl: './key-result-dialog.component.html',
  standalone: false
})
export class KeyResultDialogComponent {
  keyResultForm: FormGroup = getKeyResultForm();

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { objective: Objective;
      keyResult: KeyResult; },
    private keyResultService: KeyResultService,
    public dialogService: DialogService,
    public dialogRef: MatDialogRef<KeyResultDialogComponent>
  ) {}

  isMetricKeyResult() {
    return this.keyResultForm.controls['keyResultType'].value === 'metric';
  }

  saveKeyResult(openNewDialog = false) {
    const value = this.keyResultForm.value;
    const keyResult = this.isMetricKeyResult()
      ? ({ ...value,
        objective: this.data.objective } as KeyResultMetricDto)
      : ({ ...value,
        objective: this.data.objective } as KeyResultOrdinalDto);
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
}
