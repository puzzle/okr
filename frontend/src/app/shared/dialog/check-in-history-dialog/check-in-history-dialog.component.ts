import { Component, Inject, OnInit } from '@angular/core';
import { CheckInMin } from '../../types/model/CheckInMin';
import { CheckInService } from '../../services/check-in.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import errorMessages from '../../../../assets/errors/error-messages.json';
import { DATE_FORMAT } from '../../constantLibary';
import { KeyResult } from '../../types/model/KeyResult';
import { CheckInFormComponent } from '../checkin/check-in-form/check-in-form.component';
import { NotifierService } from '../../services/notifier.service';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { CheckIn } from '../../types/model/CheckIn';

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
    public notifierService: NotifierService,
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
      if (!result?.data) {
        let updatedCheckIn = { ...result.data, id: checkIn.id };
        this.checkInService.updateCheckIn(updatedCheckIn, updatedCheckIn.id).subscribe((updatedCheckIn) => {
          this.notifierService.reopenCheckInHistoryDialog.next({ checkIn: updatedCheckIn, deleted: false });
        });
      }
    });
  }

  deleteCheckIn(checkIn: CheckInMin) {
    this.dialogRef.close();
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Check-in',
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.checkInService.deleteCheckIn(checkIn.id).subscribe(() => {
          this.notifierService.reopenCheckInHistoryDialog.next({ checkIn: checkIn as CheckIn, deleted: true });
        });
      } else {
        this.notifierService.reopenCheckInHistoryDialog.next({ checkIn: null, deleted: false });
      }
    });
  }

  protected readonly errorMessages = errorMessages;
  protected readonly DATE_FORMAT = DATE_FORMAT;
}
