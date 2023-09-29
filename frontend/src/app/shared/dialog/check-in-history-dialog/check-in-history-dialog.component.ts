import { Component, Inject, OnInit } from '@angular/core';
import { CheckInMin } from '../../types/model/CheckInMin';
import { CheckInService } from '../../services/check-in.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import errorMessages from '../../../../assets/errors/error-messages.json';
import { DATE_FORMAT } from '../../constantLibary';
import { KeyResult } from '../../types/model/KeyResult';
import { CheckInFormComponent } from '../checkin/check-in-form/check-in-form.component';

@Component({
  selector: 'app-check-in-history-dialog',
  templateUrl: './check-in-history-dialog.component.html',
  styleUrls: ['./check-in-history-dialog.component.scss'],
})
export class CheckInHistoryDialogComponent implements OnInit {
  keyResult!: KeyResult;
  checkInHistory: CheckInMin[] = [];

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private checkInService: CheckInService,
    private dialog: MatDialog,
    public dialogRef: MatDialogRef<CheckInHistoryDialogComponent>,
  ) {}
  ngOnInit(): void {
    this.keyResult = this.data.keyResult;
    this.checkInService.getAllCheckInOfKeyResult(this.data.keyResultId).subscribe((result) => {
      this.checkInHistory = result;
    });
  }

  openCheckInDialog(checkIn: CheckInMin) {
    this.dialogRef.close();
    const dialogRef = this.dialog.open(CheckInFormComponent, {
      data: {
        keyResult: this.keyResult,
        checkIn: checkIn,
      },
      width: '719px',
    });
    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
    });
  }

  protected readonly errorMessages = errorMessages;
  protected readonly DATE_FORMAT = DATE_FORMAT;
}
