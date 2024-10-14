import { Component, Inject, OnInit } from '@angular/core';
import { CheckInMin } from '../../shared/types/model/CheckInMin';
import { CheckInService } from '../../services/check-in.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DATE_FORMAT, OKR_DIALOG_CONFIG } from '../../shared/constantLibary';
import { KeyResult } from '../../shared/types/model/KeyResult';
import { CheckInFormComponent } from '../checkin/check-in-form/check-in-form.component';
import { Observable, of } from 'rxjs';
import { KeyResultMetric } from '../../shared/types/model/KeyResultMetric';
import { RefreshDataService } from '../../services/refresh-data.service';

@Component({
  selector: 'app-check-in-history-dialog',
  templateUrl: './check-in-history-dialog.component.html',
  styleUrls: ['./check-in-history-dialog.component.scss'],
})
export class CheckInHistoryDialogComponent implements OnInit {
  keyResult!: KeyResult;
  isComplete!: boolean;
  checkInHistory$: Observable<CheckInMin[]> = of([]);
  protected readonly DATE_FORMAT = DATE_FORMAT;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private checkInService: CheckInService,
    private dialog: MatDialog,
    public dialogRef: MatDialogRef<CheckInHistoryDialogComponent>,
    private refreshDataService: RefreshDataService,
  ) {}

  ngOnInit(): void {
    this.keyResult = this.data.keyResult;
    this.isComplete = this.data.isComplete;
    this.loadCheckInHistory();
  }

  openCheckInDialogForm(checkIn: CheckInMin) {
    const dialogConfig = OKR_DIALOG_CONFIG;
    const dialogRef = this.dialog.open(CheckInFormComponent, {
      data: {
        keyResult: this.keyResult,
        checkIn: checkIn,
      },
      height: dialogConfig.height,
      width: dialogConfig.width,
      maxHeight: dialogConfig.maxHeight,
      maxWidth: dialogConfig.maxWidth,
    });
    dialogRef.afterClosed().subscribe(() => {
      this.loadCheckInHistory();
      this.refreshDataService.markDataRefresh();
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

  getMetricKeyResult(): KeyResultMetric {
    return this.keyResult as KeyResultMetric;
  }
}
