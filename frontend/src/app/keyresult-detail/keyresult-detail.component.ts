import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { KeyResult } from '../shared/types/model/KeyResult';
import { KeyresultService } from '../shared/services/keyresult.service';
import { KeyResultMetric } from '../shared/types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../shared/types/model/KeyResultOrdinal';
import { CheckInHistoryDialogComponent } from '../shared/dialog/check-in-history-dialog/check-in-history-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { BehaviorSubject, catchError, EMPTY } from 'rxjs';
import { Router } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { CloseState } from '../shared/types/enums/CloseState';
import { CheckInFormComponent } from '../shared/dialog/checkin/check-in-form/check-in-form.component';
import { State } from '../shared/types/enums/State';
import { KeyResultDialogComponent } from '../shared/dialog/key-result-dialog/key-result-dialog.component';
import { DATE_FORMAT } from '../shared/constantLibary';
import { isInValid } from '../shared/common';
import { Action } from '../shared/types/model/Action';

@Component({
  selector: 'app-keyresult-detail',
  templateUrl: './keyresult-detail.component.html',
  styleUrls: ['./keyresult-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyresultDetailComponent implements OnInit {
  @Input() keyResultId!: number;

  keyResult$: BehaviorSubject<KeyResult> = new BehaviorSubject<KeyResult>({} as KeyResult);
  isComplete: boolean = false;
  passedActionList: Action[] | null = null;
  protected readonly DATE_FORMAT = DATE_FORMAT;
  protected readonly isInValid = isInValid;

  constructor(
    private keyResultService: KeyresultService,
    private refreshDataService: RefreshDataService,
    private dialog: MatDialog,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadKeyResult();
  }

  loadKeyResult(): void {
    this.keyResultService
      .getFullKeyResult(this.keyResultId)
      .pipe(catchError(() => EMPTY))
      .subscribe((keyResult) => {
        if (this.passedActionList) {
          keyResult.actionList = this.passedActionList;
        }
        this.keyResult$.next(keyResult);
        const state = keyResult.objective.state;
        this.isComplete = state === ('SUCCESSFUL' as State) || state === ('NOTSUCCESSFUL' as State);
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
        keyResult: this.keyResult$.getValue(),
        isComplete: this.isComplete,
      },
      maxHeight: '492px',
      width: '721px',
    });
    dialogRef.afterClosed().subscribe(() => {
      this.loadKeyResult();
      this.refreshDataService.markDataRefresh();
    });
  }

  openEditKeyResultDialog(keyResult: KeyResult) {
    this.dialog
      .open(KeyResultDialogComponent, {
        width: '45em',
        height: 'auto',
        data: {
          objective: keyResult.objective,
          keyResult: keyResult,
        },
      })
      .afterClosed()
      .subscribe((result) => {
        if (result?.closeState === CloseState.SAVED) {
          this.loadKeyResult();
          this.refreshDataService.markDataRefresh();
        }
        if (result?.closeState === CloseState.DELETED) {
          this.refreshDataService.markDataRefresh();
          this.router.navigate(['/']);
        } else {
          this.loadKeyResult();
        }
      });
  }

  openCheckInForm() {
    const dialogRef = this.dialog.open(CheckInFormComponent, {
      data: {
        keyResult: this.keyResult$.getValue(),
      },
      width: '719px',
    });
    dialogRef.afterClosed().subscribe((result) => {
      this.passedActionList = result;
      this.loadKeyResult();
      this.refreshDataService.markDataRefresh();
    });
  }
}
