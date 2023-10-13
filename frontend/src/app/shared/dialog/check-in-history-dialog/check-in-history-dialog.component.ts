import { Component, Inject, OnInit } from '@angular/core';
import { CheckInMin } from '../../types/model/CheckInMin';
import { CheckInService } from '../../services/check-in.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { CONFIRM_DIALOG_WIDTH, DATE_FORMAT } from '../../constantLibary';
import { KeyResult } from '../../types/model/KeyResult';
import { CheckInFormComponent } from '../checkin/check-in-form/check-in-form.component';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'app-check-in-history-dialog',
  templateUrl: './check-in-history-dialog.component.html',
  styleUrls: ['./check-in-history-dialog.component.scss'],
})
export class CheckInHistoryDialogComponent implements OnInit {
  keyResult!: KeyResult;
  checkInHistory$: Observable<CheckInMin[]> = of([]);
  protected readonly DATE_FORMAT = DATE_FORMAT;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private checkInService: CheckInService,
    private dialog: MatDialog,
    public dialogRef: MatDialogRef<CheckInHistoryDialogComponent>,
  ) {}

  ngOnInit(): void {
    this.keyResult = this.data.keyResult;
    this.loadCheckInHistory();
  }

  openCheckInDialogForm(checkIn: CheckInMin) {
    const dialogRef = this.dialog.open(CheckInFormComponent, {
      data: {
        keyResult: this.keyResult,
        checkIn: checkIn,
      },
      width: '719px',
    });
    dialogRef.afterClosed().subscribe(() => {
      this.loadCheckInHistory();
    });
  }

  deleteCheckIn(checkIn: CheckInMin) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Check-in',
      },
      width: CONFIRM_DIALOG_WIDTH,
      height: 'auto',
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.checkInService.deleteCheckIn(checkIn.id!).subscribe(() => this.loadCheckInHistory());
      }
    });
  }

  loadCheckInHistory() {
    this.checkInHistory$ = this.checkInService.getAllCheckInOfKeyResult(this.keyResult.id);
    this.checkInHistory$.subscribe((result) => {
      if (result.length == 0) {
        this.dialogRef.close();
      }
    });
  }
}
