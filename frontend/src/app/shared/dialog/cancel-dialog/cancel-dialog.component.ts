import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export type CancelDialogData = {
  dialogTitle: string;
  dialogText?: string;
};

@Component({
  selector: 'app-cancel-dialog',
  templateUrl: './cancel-dialog.component.html',
  styleUrl: './cancel-dialog.component.scss',
})
export class CancelDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: CancelDialogData,
    private dialogRef: MatDialogRef<CancelDialogComponent>,
  ) {}

  closeAndConfirm() {
    this.dialogRef.close(true);
  }
}
