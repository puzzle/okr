import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KeyResult } from '../../types/model/KeyResult';
import { now } from 'moment';

@Component({
  selector: 'app-check-in-form',
  templateUrl: './check-in-form.component.html',
  styleUrls: ['./check-in-form.component.scss'],
})
export class CheckInFormComponent implements OnInit {
  keyResult: KeyResult;
  currentDate: Date;
  constructor(
    public dialogRef: MatDialogRef<CheckInFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.keyResult = data.keyResult;
    this.currentDate = new Date();
  }

  ngOnInit(): void {
    console.log(this.keyResult.createdOn);
  }

  protected readonly now = now;
  protected readonly Date = Date;
}
