import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { formInputCheck } from '../../common';
import errorMessages from '../../../../assets/errors/error-messages.json';

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

  constructor(
    public dialogRef: MatDialogRef<CompleteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { objectiveTitle: string },
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

  isTouchedOrDirty(name: string) {
    return this.completeForm.get(name)?.dirty || this.completeForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.completeForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  protected readonly errorMessages: any = errorMessages;
}
