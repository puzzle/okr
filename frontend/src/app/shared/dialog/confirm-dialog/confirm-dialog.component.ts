import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SpinnerService } from '../../services/spinner/spinner.service';

@Component({
  selector: 'app-keyresult-delete-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss'],
})
export class ConfirmDialogComponent implements OnInit {
  title: string;
  confirmText: string;
  closeText: string;
  constructor(
    private dialogRef: MatDialogRef<ConfirmDialogComponent>,
    public spinnerService: SpinnerService,
    @Inject(MAT_DIALOG_DATA) data: any
  ) {
    this.title = data.title;
    this.confirmText = data.confirmText;
    this.closeText = data.closeText;
  }

  ngOnInit(): void {}

  close() {
    this.dialogRef.close(false);
  }

  confirm() {
    this.dialogRef.close(true);
  }
}
