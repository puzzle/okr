import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges } from '@angular/core';
import { KeyResult } from '../shared/types/model/KeyResult';
import { KeyresultService } from '../shared/services/keyresult.service';
import { KeyResultMetric } from '../shared/types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../shared/types/model/KeyResultOrdinal';
import { CheckInHistoryDialogComponent } from '../shared/dialog/check-in-history-dialog/check-in-history-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';
import { NotifierService } from '../shared/services/notifier.service';

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
        if (result.openNew) {
          this.openEditKeyResultDialog();
        } else {
          if (result.delete) {
            await this.notifierService.deleteKeyResult.next(result.keyResult);
          } else {
            await this.notifierService.keyResultsChanges.next({
              keyResult: result.keyResult,
              changeId: result.changeId,
              objective: result.objective,
            });
          }
        }
      });
  }
}
