import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss'],
})
export class ConfirmDialogComponent implements OnInit {
  dialogTitle: string = '';
  dialogText: string = '';
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    private translate: TranslateService,
  ) {}

  ngOnInit() {
    this.dialogTitle = this.data.title;
    this.dialogText = this.data.text;
  }

  closeAndDelete() {
    this.dialogRef.close(true);
  }
}
