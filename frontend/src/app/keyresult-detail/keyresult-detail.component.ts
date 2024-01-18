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
import { CONFIRM_DIALOG_WIDTH, DATE_FORMAT } from '../shared/constantLibary';
import { isLastCheckInNegative, isMobileDevice } from '../shared/common';
import { KeyresultDialogComponent } from '../shared/dialog/keyresult-dialog/keyresult-dialog.component';
import { ConfirmDialogComponent } from '../shared/dialog/confirm-dialog/confirm-dialog.component';

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
  protected readonly isLastCheckInNegative = isLastCheckInNegative;

  constructor(
    private keyResultService: KeyresultService,
    private refreshDataService: RefreshDataService,
    private dialog: MatDialog,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadKeyResult(this.keyResultId);
  }

  loadKeyResult(id: number): void {
    this.keyResultService
      .getFullKeyResult(id)
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
        if (result?.closeState === CloseState.SAVED && result.id) {
          this.loadKeyResult(result.id);
          this.refreshDataService.markDataRefresh();
        } else if (result?.closeState === CloseState.DELETED) {
          this.router.navigate(['']).then(() => this.refreshDataService.markDataRefresh());
        } else {
          this.loadKeyResult(this.keyResult$.getValue().id);
        }
      });
  }

  checkForDraftState(keyResult: KeyResult) {
    if (keyResult.objective.state.toUpperCase() === 'DRAFT') {
      const dialogConfig = isMobileDevice()
        ? {
            maxWidth: '100vw',
            maxHeight: '100vh',
            height: '100vh',
            width: CONFIRM_DIALOG_WIDTH,
          }
        : {
            width: '45em',
            height: 'auto',
          };

      this.dialog
        .open(ConfirmDialogComponent, {
          data: {
            draftCreate: true,
          },
          width: dialogConfig.width,
          height: dialogConfig.height,
          maxHeight: dialogConfig.maxHeight,
          maxWidth: dialogConfig.maxWidth,
        })
        .afterClosed()
        .subscribe((result) => {
          if (result) {
            this.openCheckInForm();
          }
        });
    } else {
      this.openCheckInForm();
    }
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
      this.loadKeyResult(this.keyResult$.getValue().id);
      this.refreshDataService.markDataRefresh();
    });
  }

  backToOverview() {
    this.router.navigate(['']);
  }
}
