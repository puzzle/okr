import { Component, Inject, OnInit } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogConfig,
  MatDialogRef,
} from '@angular/material/dialog';
import { KeyValue } from '@angular/common';
import { DialogConfig } from '@angular/cdk/dialog';

@Component({
  selector: 'app-keyresult-delete-dialog',
  templateUrl: './keyresult-delete-dialog.component.html',
  styleUrls: ['./keyresult-delete-dialog.component.scss'],
})
export class KeyresultDeleteDialogComponent implements OnInit {
  constructor(
    private dialogRef: MatDialogRef<KeyresultDeleteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: MatDialogConfig
  ) {}

  ngOnInit(): void {}

  close() {
    this.dialogRef.close(false);
  }

  confirm() {
    this.dialogRef.close(true);
  }
}
