import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-keyresult-delete-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss'],
})
export class ConfirmDialogComponent {
  closeDialog: Subject<boolean> = new Subject<boolean>();
  title: string;
  confirmText: string;
  closeText: string;
  displaySpinner: boolean = false;
  constructor(
    private dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any
  ) {
    this.title = data.title;
    this.confirmText = data.confirmText;
    this.closeText = data.closeText;
  }

  close() {
    this.closeDialog.next(false);
  }

  confirm() {
    this.closeDialog.next(true);
  }
}
