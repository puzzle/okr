import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
})
export class ConfirmDialogComponent implements OnInit {
  dialogTitle: string = '';
  dialogText: string = '';
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
  ) {}

  ngOnInit() {
    if (this.data.action) {
      if (this.data.action === 'release') {
        this.dialogTitle = this.data.title + ' freigeben';
        this.dialogText = 'Soll dieses ' + this.data.title + ' freigegeben werden?';
      }
    } else {
      this.dialogTitle = this.data.title + ' löschen';
      this.dialogText = (this.data.isAction ? 'Soll diese ' : 'Soll dieses ') + this.data.title + ' gelöscht werden?';
    }
  }

  closeAndDelete() {
    this.dialogRef.close(true);
  }
}
