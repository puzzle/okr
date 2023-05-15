import { Component, Inject, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface HelpText {
  description: string;
  exampleTitle?: string;
  examples?: string[];
}

@Component({
  selector: 'app-help-dialog',
  templateUrl: './help-dialog.component.html',
  styleUrls: ['./help-dialog.component.scss'],
})
export class HelpDialogComponent {
  closeDialog: Subject<boolean> = new Subject<boolean>();
  title: string;
  exampleTitle?: string;
  examples?: string[];
  displaySpinner: boolean = false;
  constructor(private dialogRef: MatDialogRef<HelpDialogComponent>, @Inject(MAT_DIALOG_DATA) data: HelpText) {
    this.title = data.description;
    this.exampleTitle = data.exampleTitle;
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
