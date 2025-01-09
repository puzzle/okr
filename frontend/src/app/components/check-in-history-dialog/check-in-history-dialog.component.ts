import { Component, Inject, OnInit } from '@angular/core';
import { CheckInMin } from '../../shared/types/model/check-in-min';
import { CheckInService } from '../../services/check-in.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DATE_FORMAT } from '../../shared/constant-library';
import { KeyResult } from '../../shared/types/model/key-result';
import { CheckInFormComponent } from '../checkin/check-in-form/check-in-form.component';
import { Observable, of } from 'rxjs';
import { KeyResultMetric } from '../../shared/types/model/key-result-metric';
import { RefreshDataService } from '../../services/refresh-data.service';
import { DialogService } from '../../services/dialog.service';
import { CheckInMetricMin } from '../../shared/types/model/check-in-metric-min';
import { CheckInOrdinalMin } from '../../shared/types/model/check-in-ordinal-min';
import { CheckIn } from '../../shared/types/model/check-in';

@Component({
  selector: 'app-check-in-history-dialog',
  templateUrl: './check-in-history-dialog.component.html',
  standalone: false
})
export class CheckInHistoryDialogComponent implements OnInit {
  keyResult!: KeyResult;

  isComplete!: boolean;

  checkInHistory$: Observable<CheckIn[]> = of([]);

  protected readonly DATE_FORMAT = DATE_FORMAT;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private checkInService: CheckInService,
    private dialogService: DialogService,
    public dialogRef: MatDialogRef<CheckInHistoryDialogComponent>,
    private refreshDataService: RefreshDataService
  ) {}

  ngOnInit(): void {
    this.keyResult = this.data.keyResult;
    this.isComplete = this.data.isComplete;
    this.loadCheckInHistory();
  }

  openCheckInDialogForm(checkIn: CheckInMin) {
    const dialogRef = this.dialogService.open(CheckInFormComponent, {
      data: {
        keyResult: this.keyResult,
        checkIn: checkIn
      }
    });
    dialogRef.afterClosed()
      .subscribe(() => {
        this.loadCheckInHistory();
        this.refreshDataService.reloadKeyResultSubject.next();
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

  getCheckInMetric(checkIn: CheckInMin): CheckInMetricMin {
    return checkIn as CheckInMetricMin;
  }

  getCheckInOrdinal(checkIn: CheckInMin): CheckInOrdinalMin {
    return checkIn as CheckInOrdinalMin;
  }
}
