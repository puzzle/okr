import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges } from '@angular/core';
import { KeyResult } from '../shared/types/model/KeyResult';
import { KeyresultService } from '../shared/services/keyresult.service';
import { KeyResultMetric } from '../shared/types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../shared/types/model/KeyResultOrdinal';
import { CheckInHistoryDialogComponent } from '../shared/dialog/check-in-history-dialog/check-in-history-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { CheckInFormMetricComponent } from '../shared/dialog/checkin/check-in-form-metric/check-in-form-metric.component';
import { KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';
import { NotifierService } from '../shared/services/notifier.service';
import { CheckInService } from '../shared/services/check-in.service';
import { CheckInFormComponent } from '../shared/dialog/checkin/check-in-form/check-in-form.component';

@Component({
  selector: 'app-keyresult-detail',
  templateUrl: './keyresult-detail.component.html',
  styleUrls: ['./keyresult-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyresultDetailComponent implements OnChanges {
  @Input() keyResultId!: number;
  keyResult!: KeyResult;

  constructor(
    private keyResultService: KeyresultService,
    private checkInService: CheckInService,
    private notifierService: NotifierService,
    private changeDetectorRef: ChangeDetectorRef,
    private dialog: MatDialog,
  ) {
    this.notifierService.reopenCheckInHistoryDialog.subscribe((result) => {
      /* LastCheckIn has been updated */
      if (this.keyResult.lastCheckIn?.id === result?.checkIn?.id) {
        this.keyResult = { ...this.keyResult, lastCheckIn: result.checkIn };
        this.changeDetectorRef.detectChanges();
      }
      /* CheckIn was deleted */
      if (result.deleted) {
        if (result.checkIn?.id == this.keyResult.lastCheckIn?.id) {
          this.keyResultService.getFullKeyResult(this.keyResultId).subscribe((fullKeyResult) => {
            this.keyResult = fullKeyResult;
            this.changeDetectorRef.markForCheck();
            if (this.keyResult.lastCheckIn != null) {
              this.checkInHistory();
            }
          });
        }
        return;
      }
      this.checkInHistory();
    });
  }

  ngOnChanges() {
    this.keyResultService.getFullKeyResult(this.keyResultId).subscribe((fullKeyResult) => {
      this.keyResult = fullKeyResult;
      this.changeDetectorRef.markForCheck();
    });
  }

  castToMetric(keyResult: KeyResult) {
    return keyResult as KeyResultMetric;
  }

  castToOrdinal(keyResult: KeyResult) {
    return keyResult as KeyResultOrdinal;
  }

  checkInHistory() {
    const dialogRef = this.dialog.open(CheckInHistoryDialogComponent, {
      data: {
        keyResultId: this.keyResult.id,
        keyResult: this.keyResult,
      },
    });
    dialogRef.afterClosed().subscribe(() => {});
  }

  openEditKeyResultDialog() {
    this.dialog
      .open(KeyResultDialogComponent, {
        width: '45em',
        height: 'auto',
        data: {
          objective: null,
          keyResult: this.keyResult,
        },
      })
      .afterClosed()
      .subscribe(async (result) => {
        await this.notifierService.keyResultsChanges.next({
          keyResult: result.keyResult,
          changeId: result.changeId,
          objective: result.objective,
          delete: result.delete,
        });
        if (result.openNew) {
          this.openEditKeyResultDialog();
        }

        this.keyResult = {
          ...this.keyResult,
          id: result.keyResult.id,
          title: result.keyResult.title,
          description: result.keyResult.description,
        };
        this.changeDetectorRef.markForCheck();
      });
  }

  openCheckInForm() {
    const dialogRef = this.dialog.open(CheckInFormComponent, {
      data: {
        keyResult: this.keyResult,
      },
      width: '719px',
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result != undefined && result != '') {
        this.checkInService.createCheckIn(result.data).subscribe((createdCheckIn) => {
          this.keyResult = { ...this.keyResult, lastCheckIn: createdCheckIn };
          this.changeDetectorRef.detectChanges();
        });
      }
    });
  }
}
