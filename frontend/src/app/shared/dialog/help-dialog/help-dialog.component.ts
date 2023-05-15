import { Component, Inject, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-help-dialog',
  templateUrl: './help-dialog.component.html',
  styleUrls: ['./help-dialog.component.scss'],
})
export class HelpDialogComponent {
  closeDialog: Subject<boolean> = new Subject<boolean>();
  title: string;
  examples: string[];
  displaySpinner: boolean = false;
  constructor(private dialogRef: MatDialogRef<HelpDialogComponent>, @Inject(MAT_DIALOG_DATA) data: any) {
    this.title = data.title;
    this.examples = data.examples;
  }

  close() {
    this.closeDialog.next(false);
  }

  confirm() {
    this.closeDialog.next(true);
  }

  reload() {}
}
