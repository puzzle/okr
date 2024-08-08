import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export type AlertDialogData = {
  dialogTitle: string;
  dialogText?: string;
  dialogDetails?: string[];
};

@Component({
  selector: 'app-alert-dialog',
  templateUrl: './alert-dialog.component.html',
  styleUrl: './alert-dialog.component.scss',
})
export class AlertDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: AlertDialogData,
    private dialogRef: MatDialogRef<AlertDialogComponent>,
  ) {}

  closeDialog() {
    this.dialogRef.close();
  }
}
