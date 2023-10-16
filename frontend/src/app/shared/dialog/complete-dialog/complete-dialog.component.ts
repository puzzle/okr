import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { State } from '../../types/enums/State';

@Component({
  selector: 'app-complete-dialog',
  templateUrl: './complete-dialog.component.html',
  styleUrls: ['./complete-dialog.component.scss'],
})
export class CompleteDialogComponent {
  isSuccessful: boolean = true;
  comment: string = '';

  constructor(public dialogRef: MatDialogRef<CompleteDialogComponent>) {}

  closeDialog() {
    this.dialogRef.close({
      endState: this.isSuccessful ? State.SUCCESSFUL : State.NOTSUCCESSFUL,
      comment: this.comment,
    });
  }
}
