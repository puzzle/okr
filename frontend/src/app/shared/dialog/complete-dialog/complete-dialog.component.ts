import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-complete-dialog',
  templateUrl: './complete-dialog.component.html',
  styleUrls: ['./complete-dialog.component.scss'],
})
export class CompleteDialogComponent {
  completeForm = new FormGroup({
    isSuccessful: new FormControl<boolean>(true, [Validators.required]),
    comment: new FormControl<string>('', [Validators.maxLength(4096)]),
  });

  constructor(public dialogRef: MatDialogRef<CompleteDialogComponent>) {}

  closeDialog() {
    this.dialogRef.close({
      endState: this.completeForm.value.isSuccessful ? 'SUCCESSFUL' : 'NOTSUCCESSFUL',
      comment: this.completeForm.value.comment,
    });
  }
}
