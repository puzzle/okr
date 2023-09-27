import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges } from '@angular/core';
import { KeyResult } from '../shared/types/model/KeyResult';
import { KeyresultService } from '../shared/services/keyresult.service';
import { KeyResultMetric } from '../shared/types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../shared/types/model/KeyResultOrdinal';
import { CheckInHistoryDialogComponent } from '../shared/dialog/check-in-history-dialog/check-in-history-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';
import { NotifierService } from '../shared/services/notifier.service';
import { CheckInFormComponent } from '../shared/dialog/check-in-form/check-in-form.component';
import { CheckInFormMetricComponent } from '../shared/dialog/check-in-form-metric/check-in-form-metric.component';
import { CheckInService } from '../shared/services/check-in.service';

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
    private changeDetectorRef: ChangeDetectorRef,
    private dialog: MatDialog,
    private notifierService: NotifierService,
  ) {}

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
    const dialogRef = this.dialog.open(CheckInFormMetricComponent, {
      data: {
        keyResult: this.keyResult,
      },
      width: '719px',
    });
    dialogRef.afterClosed().subscribe((result) => {
      this.checkInService.createKeyResult(result.data).subscribe();
    });
  }
}
