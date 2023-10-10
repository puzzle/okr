import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
import { KeyResult } from '../shared/types/model/KeyResult';
import { KeyresultService } from '../shared/services/keyresult.service';
import { KeyResultMetric } from '../shared/types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../shared/types/model/KeyResultOrdinal';
import { CheckInHistoryDialogComponent } from '../shared/dialog/check-in-history-dialog/check-in-history-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';
import { CheckInService } from '../shared/services/check-in.service';
import { catchError, Observable, Subject } from 'rxjs';
import { Router } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { CloseState } from '../shared/types/enums/CloseState';

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
    private checkInService: CheckInService,
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

  openCheckInForm() {
    // const dialogRef = this.dialog.open(CheckInFormComponent, {
    //   data: {
    //     keyResult: this.keyResult,
    //   },
    //   width: '719px',
    // });
    // dialogRef.afterClosed().subscribe((result) => {
    //   if (result != undefined && result != '') {
    //     this.checkInService.createCheckIn(result.data).subscribe((createdCheckIn) => {
    //       this.keyResult = { ...this.keyResult, lastCheckIn: createdCheckIn };
    //       this.changeDetectorRef.detectChanges();
    //     });
    //   }
    // });
  }
}
