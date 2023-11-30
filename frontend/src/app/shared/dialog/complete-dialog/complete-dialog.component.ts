import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { formInputCheck, hasFormFieldErrors } from '../../common';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-complete-dialog',
  templateUrl: './complete-dialog.component.html',
  styleUrls: ['./complete-dialog.component.scss'],
})
export class CompleteDialogComponent {
  completeForm = new FormGroup({
    isSuccessful: new FormControl<boolean | null>(null, [Validators.required]),
    comment: new FormControl<string | null>(null, [Validators.maxLength(4096)]),
  });
  protected readonly formInputCheck = formInputCheck;
  protected readonly hasFormFieldErrors = hasFormFieldErrors;

  constructor(
    public dialogRef: MatDialogRef<CompleteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { objectiveTitle: string },
    private translate: TranslateService,
  ) {}

  switchSuccessState(input: string) {
    this.removeStandardHover();
    let successfulValue = this.completeForm.value.isSuccessful;
    if (
      successfulValue == null ||
      (input == 'successful' && !successfulValue) ||
      (input == 'notSuccessful' && successfulValue)
    ) {
      successfulValue = input == 'successful';
      this.completeForm.patchValue({ isSuccessful: successfulValue });
    }
  }

  closeDialog() {
    this.dialogRef.close({
      endState: this.completeForm.value.isSuccessful ? 'SUCCESSFUL' : 'NOTSUCCESSFUL',
      comment: this.completeForm.value.comment,
    });
  }

  removeStandardHover() {
    let elements = document.querySelectorAll('.card-hover');
    elements.forEach((el) => {
      el.classList.remove('card-hover');
    });
  }

  getErrorMessage(error: string, field: string, maxLength: number | null): string {
    return field + this.translate.instant('DIALOG_ERRORS.' + error).format(maxLength);
  }
}
