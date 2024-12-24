import { ChangeDetectionStrategy, Component, Input, OnDestroy, OnInit } from '@angular/core';
import { KeyResult } from '../../shared/types/model/KeyResult';
import { KeyResultService } from '../../services/key-result.service';
import { KeyResultMetric } from '../../shared/types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../../shared/types/model/KeyResultOrdinal';
import { CheckInHistoryDialogComponent } from '../check-in-history-dialog/check-in-history-dialog.component';
import { BehaviorSubject, catchError, EMPTY, Subject, takeUntil } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { RefreshDataService } from '../../services/refresh-data.service';
import { CloseState } from '../../shared/types/enums/CloseState';
import { CheckInFormComponent } from '../checkin/check-in-form/check-in-form.component';
import { State } from '../../shared/types/enums/State';
import { DATE_FORMAT } from '../../shared/constantLibary';
import { calculateCurrentPercentage } from '../../shared/common';
import { KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';
import { DialogService } from '../../services/dialog.service';
import { KeyResultMin } from '../../shared/types/model/KeyResultMin';
import { KeyResultMetricMin } from '../../shared/types/model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from '../../shared/types/model/KeyResultOrdinalMin';

@Component({
  selector: 'app-key-result-detail',
  templateUrl: './key-result-detail.component.html',
  styleUrls: ['./key-result-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class KeyResultDetailComponent implements OnInit, OnDestroy {
  @Input() keyResultId!: number;

  keyResult$: BehaviorSubject<KeyResult> = new BehaviorSubject<KeyResult>({} as KeyResult);

  ngDestroy$ = new Subject<void>();

  isComplete = false;

  protected readonly DATE_FORMAT = DATE_FORMAT;

  constructor(
    private keyResultService: KeyResultService,
    private refreshDataService: RefreshDataService,
    private dialogService: DialogService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.keyResultId = this.getIdFromParams();
    this.loadKeyResult(this.keyResultId);
    this.refreshDataService.reloadKeyResultSubject.pipe(takeUntil(this.ngDestroy$))
      .subscribe(() => {
        this.loadKeyResult(this.keyResultId);
      });
  }

  ngOnDestroy() {
    this.ngDestroy$.next();
    this.ngDestroy$.complete();
  }

  private getIdFromParams(): number {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      throw Error('keyresult id is undefined');
    }
    return parseInt(id);
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

  castToOrdinal(keyResult: KeyResult): KeyResultOrdinal {
    return keyResult as KeyResultOrdinal;
  }

  checkInHistory() {
    const dialogRef = this.dialogService.open(CheckInHistoryDialogComponent, {
      data: {
        keyResult: this.keyResult$.getValue(),
        isComplete: this.isComplete
      }
    });
    dialogRef.afterClosed()
      .subscribe(() => {
        this.refreshDataService.markDataRefresh();
      });
  }

  openEditKeyResultDialog(keyResult: KeyResult) {
    this.dialogService
      .open(KeyResultDialogComponent, {
        data: {
          objective: keyResult.objective,
          keyResult: keyResult
        }
      })
      .afterClosed()
      .subscribe((result) => {
        if (result?.closeState === CloseState.SAVED && result.id) {
          this.loadKeyResult(result.id);
          this.refreshDataService.markDataRefresh();
        } else if (result?.closeState === CloseState.DELETED) {
          this.router.navigate([''])
            .then(() => this.refreshDataService.markDataRefresh());
        } else {
          this.loadKeyResult(this.keyResult$.getValue().id);
        }
      });
  }

  checkForDraftState(keyResult: KeyResult) {
    if (keyResult.objective.state.toUpperCase() === 'DRAFT') {
      this.dialogService
        .openConfirmDialog('CONFIRMATION.DRAFT_CREATE')
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
    const dialogRef = this.dialogService.open(CheckInFormComponent, {
      data: {
        keyResult: this.keyResult$.getValue()
      }
    });
    dialogRef.afterClosed()
      .subscribe(() => {
        this.refreshDataService.reloadKeyResultSubject.next();
        this.refreshDataService.markDataRefresh();
      });
  }

  backToOverview() {
    this.router.navigate(['']);
  }

  getKeyResultWithCorrectType(keyResult: KeyResult): KeyResultOrdinalMin | KeyResultMetricMin {
    if (keyResult.keyResultType === 'metric') {
      return keyResult as KeyResultMin as KeyResultMetricMin;
    } else {
      return keyResult as KeyResultMin as KeyResultOrdinalMin;
    }
  }

  protected readonly calculateCurrentPercentage = calculateCurrentPercentage;
}
