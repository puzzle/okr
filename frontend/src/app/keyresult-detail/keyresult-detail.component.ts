import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { KeyResult } from '../shared/types/model/KeyResult';
import { KeyresultService } from '../shared/services/keyresult.service';
import { KeyResultMetric } from '../shared/types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../shared/types/model/KeyResultOrdinal';
import { CheckInHistoryDialogComponent } from '../shared/dialog/check-in-history-dialog/check-in-history-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { CloseState, KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';
import { catchError, Observable, Subject } from 'rxjs';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-keyresult-detail',
  templateUrl: './keyresult-detail.component.html',
  styleUrls: ['./keyresult-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyresultDetailComponent implements OnInit {
  @Input()
  keyResultId$!: Observable<number>;

  keyResult$: Subject<KeyResult> = new Subject<KeyResult>();

  constructor(
    private keyResultService: KeyresultService,
    private dialog: MatDialog,
    private refreshDataService: RefreshDataService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.keyResultId$.subscribe((id) => {
      this.loadKeyResult(id);
    });
  }

  loadKeyResult(id: number): void {
    this.keyResultService
      .getFullKeyResult(id)
      .pipe(
        catchError((err, caught) => {
          console.error(err);
          // TODO: maybe return a EMPTY or NEVER
          return caught;
        }),
      )
      .subscribe((keyResult) => this.keyResult$.next(keyResult));
  }

  castToMetric(keyResult: KeyResult) {
    return keyResult as KeyResultMetric;
  }
  castToOrdinal(keyResult: KeyResult) {
    return keyResult as KeyResultOrdinal;
  }
  checkInHistory(keyResultId: number) {
    const dialogRef = this.dialog.open(CheckInHistoryDialogComponent, {
      data: {
        keyResultId: keyResultId,
      },
    });
    dialogRef.afterClosed().subscribe(() => {});
  }

  openEditKeyResultDialog(keyResult: KeyResult) {
    this.dialog
      .open(KeyResultDialogComponent, {
        width: '45em',
        height: 'auto',
        data: {
          objective: null,
          keyResult: keyResult,
        },
      })
      .afterClosed()
      .subscribe((result) => {
        console.log('result.id', result.id);
        console.log('result.closeState', result.closeState);
        if (result.closeState === CloseState.SAVED) {
          this.loadKeyResult(result.id);
          this.refreshDataService.markDataRefresh();
        }
        if (result.closeState === CloseState.DELETED) {
          this.refreshDataService.markDataRefresh();
          this.router.navigate(['/']);
        }
      });
  }
}
