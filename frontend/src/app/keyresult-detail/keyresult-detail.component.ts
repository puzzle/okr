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
import { DATE_FORMAT } from '../shared/constantLibary';
import { isInValid, isMobileDevice } from '../shared/common';
import { KeyresultDialogComponent } from '../shared/dialog/keyresult-dialog/keyresult-dialog.component';

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
  protected readonly DATE_FORMAT = DATE_FORMAT;
  protected readonly isInValid = isInValid;

  constructor(
    private keyResultService: KeyresultService,
    private refreshDataService: RefreshDataService,
    private dialog: MatDialog,
    private router: Router,
  ) {
    this.refreshDataService.reloadOverviewSubject.subscribe(() => this.loadKeyResult());
  }

  ngOnInit(): void {
    this.loadKeyResult();
  }

  loadKeyResult(): void {
    this.keyResultService
      .getFullKeyResult(this.keyResultId)
      .pipe(catchError(() => EMPTY))
      .subscribe((keyResult) => {
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
    const dialogConfig = isMobileDevice()
      ? {
          maxWidth: '100vw',
          maxHeight: '100vh',
          height: '100vh',
          width: '100vw',
        }
      : {
          width: '45em',
          height: 'auto',
        };
    const dialogRef = this.dialog.open(CheckInHistoryDialogComponent, {
      data: {
        keyResult: this.keyResult$.getValue(),
        isComplete: this.isComplete,
      },
      width: dialogConfig.width,
      height: dialogConfig.height,
      maxHeight: dialogConfig.maxHeight,
      maxWidth: dialogConfig.maxWidth,
    });
    dialogRef.afterClosed().subscribe(() => {
      this.refreshDataService.markDataRefresh();
    });
  }

  openEditKeyResultDialog(keyResult: KeyResult) {
    const dialogConfig = isMobileDevice()
      ? {
          maxWidth: '100vw',
          maxHeight: '100vh',
          height: '100vh',
          width: '100vw',
        }
      : {
          width: '45em',
          height: 'auto',
        };

    this.dialog
      .open(KeyresultDialogComponent, {
        height: dialogConfig.height,
        width: dialogConfig.width,
        maxHeight: dialogConfig.maxHeight,
        maxWidth: dialogConfig.maxWidth,
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
    const dialogConfig = isMobileDevice()
      ? {
          maxWidth: '100vw',
          maxHeight: '100vh',
          height: '100vh',
          width: '100vw',
        }
      : {
          width: '45em',
          height: 'auto',
        };
    const dialogRef = this.dialog.open(CheckInFormComponent, {
      height: dialogConfig.height,
      width: dialogConfig.width,
      maxHeight: dialogConfig.maxHeight,
      maxWidth: dialogConfig.maxWidth,
      data: {
        keyResult: this.keyResult$.getValue(),
      },
    });
    dialogRef.afterClosed().subscribe(() => {
      this.loadKeyResult();
      this.refreshDataService.markDataRefresh();
    });
  }
}
