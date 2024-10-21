import { ChangeDetectionStrategy, Component, Input, OnDestroy, OnInit } from '@angular/core';
import { KeyResult } from '../../shared/types/model/KeyResult';
import { KeyresultService } from '../../services/keyresult.service';
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
import { calculateCurrentPercentage, isLastCheckInNegative, isMobileDevice } from '../../shared/common';
import { KeyresultDialogComponent } from '../keyresult-dialog/keyresult-dialog.component';
import { DialogService } from '../../services/dialog.service';

@Component({
  selector: 'app-keyresult-detail',
  templateUrl: './keyresult-detail.component.html',
  styleUrls: ['./keyresult-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyresultDetailComponent implements OnInit, OnDestroy {
  @Input() keyResultId!: number;

  keyResult$: BehaviorSubject<KeyResult> = new BehaviorSubject<KeyResult>({} as KeyResult);
  ngDestroy$: Subject<void> = new Subject();
  isComplete: boolean = false;
  protected readonly DATE_FORMAT = DATE_FORMAT;
  protected readonly isLastCheckInNegative = isLastCheckInNegative;

  constructor(
    private keyResultService: KeyresultService,
    private refreshDataService: RefreshDataService,
    private dialogService: DialogService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.keyResultId = this.getIdFromParams();
    this.loadKeyResult(this.keyResultId);
    this.refreshDataService.reloadKeyResultSubject.pipe(takeUntil(this.ngDestroy$)).subscribe(() => {
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

  castToOrdinal(keyResult: KeyResult) {
    return keyResult as KeyResultOrdinal;
  }

  checkInHistory() {
    const dialogRef = this.dialogService.open(CheckInHistoryDialogComponent, {
      data: {
        keyResult: this.keyResult$.getValue(),
        isComplete: this.isComplete,
      },
    });
    dialogRef.afterClosed().subscribe(() => {
      this.refreshDataService.markDataRefresh();
    });
  }

  openEditKeyResultDialog(keyResult: KeyResult) {
    this.dialogService
      .open(KeyresultDialogComponent, {
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
      this.dialogService
        .openConfirmDialog('CONFIRMATION.CREATE_DRAFT_KEYRESULT')
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
        keyResult: this.keyResult$.getValue(),
      },
    });
    dialogRef.afterClosed().subscribe(() => {
      this.refreshDataService.reloadKeyResultSubject.next();
      this.refreshDataService.markDataRefresh();
    });
  }

  backToOverview() {
    this.router.navigate(['']);
  }

  protected readonly calculateCurrentPercentage = calculateCurrentPercentage;
}
