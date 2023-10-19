import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-complete-dialog',
  templateUrl: './complete-dialog.component.html',
  styleUrls: ['./complete-dialog.component.scss'],
})
export class CompleteDialogComponent {
  isSuccessful: boolean = true;
  completeForm = new FormGroup({
    comment: new FormControl<string>('', [Validators.maxLength(4096)]),
  });

  constructor(public dialogRef: MatDialogRef<CompleteDialogComponent>) {}

  switchSuccessState(input: string) {
    if ((input == 'successful' && !this.isSuccessful) || (input == 'notSuccessful' && this.isSuccessful)) {
      this.isSuccessful = !this.isSuccessful;
    }
  }

  closeDialog() {
    this.dialogRef.close({
      endState: this.isSuccessful ? 'SUCCESSFUL' : 'NOTSUCCESSFUL',
      comment: this.completeForm.value.comment,
    });
  }
}
