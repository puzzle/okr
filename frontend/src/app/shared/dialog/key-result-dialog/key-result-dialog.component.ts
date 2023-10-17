import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from '../../types/model/User';
import { KeyResult } from '../../types/model/KeyResult';
import { KeyresultService } from '../../services/keyresult.service';
import { KeyResultMetricDTO } from '../../types/DTOs/KeyResultMetricDTO';
import { testUser } from '../../testData';
import errorMessages from '../../../../assets/errors/error-messages.json';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { Objective } from '../../types/model/Objective';
import { KeyResultEmitOrdinalDTO } from '../../types/DTOs/KeyResultEmitOrdinalDTO';
import { KeyResultEmitDTO } from '../../types/DTOs/KeyResultEmitDTO';
import { KeyResultEmitMetricDTO } from '../../types/DTOs/KeyResultEmitMetricDTO';
import { KeyResultDTO } from '../../types/DTOs/KeyResultDTO';
import { KeyResultOrdinalDTO } from '../../types/DTOs/KeyResultOrdinalDTO';
import { KeyResultMetric } from '../../types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../../types/model/KeyResultOrdinal';
import { filter, map, Observable, of, share, startWith, switchMap } from 'rxjs';
import { UserService } from '../../services/user.service';
import { CloseState } from '../../types/enums/CloseState';

@Component({
  selector: 'app-key-result-dialog',
  templateUrl: './key-result-dialog.component.html',
  styleUrls: ['./key-result-dialog.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyResultDialogComponent implements OnInit {
  isMetricKeyResult: boolean = true;
  users$!: Observable<User[]>;
  filteredUsers$: Observable<User[]> | undefined = of([]);

  keyResultForm = new FormGroup({
    title: new FormControl<string>('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    owner: new FormControl<User | string | null>(null, [Validators.required, Validators.nullValidator]),
    unit: new FormControl<string | null>(null, [Validators.required]),
    baseline: new FormControl<number | null>(null, [Validators.required, Validators.pattern('^-?\\d+\\.?\\d*$')]),
    stretchGoal: new FormControl<number | null>(null, [Validators.required, Validators.pattern('^-?\\d+\\.?\\d*$')]),
    commitZone: new FormControl<string | null>(null, [Validators.required, Validators.maxLength(400)]),
    targetZone: new FormControl<string | null>(null, [Validators.required, Validators.maxLength(400)]),
    stretchZone: new FormControl<string | null>(null, [Validators.required, Validators.maxLength(400)]),
  });
  protected readonly errorMessages: any = errorMessages;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { objective: Objective; keyResult: KeyResult },
    public dialogRef: MatDialogRef<KeyResultDialogComponent>,
    private keyResultService: KeyresultService,
    public dialog: MatDialog,
    public userService: UserService,
  ) {}

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
    this.filteredUsers$ = this.keyResultForm.get('owner')?.valueChanges.pipe(
      startWith(''),
      filter((value) => typeof value === 'string'),
      switchMap((value) => this.filter(value as string)),
    );

    if (this.data.keyResult) {
      this.keyResultForm.controls.title.setValue(this.data.keyResult.title);
      this.keyResultForm.controls.description.setValue(this.data.keyResult.description);
      this.keyResultForm.controls.owner.setValue(this.data.keyResult.owner);

      this.isMetricKeyResult = this.data.keyResult.keyResultType == 'metric';
      if (this.isMetricKeyResult) {
        let keyResultMetric = this.data.keyResult as KeyResultMetric;
        this.keyResultForm.controls.unit.setValue(keyResultMetric.unit);
        this.keyResultForm.controls.baseline.setValue(keyResultMetric.baseline);
        this.keyResultForm.controls.stretchGoal.setValue(keyResultMetric.stretchGoal);
      } else {
        let keyResultOrdinal = this.data.keyResult as KeyResultOrdinal;
        this.keyResultForm.controls.commitZone.setValue(keyResultOrdinal.commitZone);
        this.keyResultForm.controls.targetZone.setValue(keyResultOrdinal.targetZone);
        this.keyResultForm.controls.stretchZone.setValue(keyResultOrdinal.stretchZone);
      }
    }
  }

  saveKeyResult(openNewDialog = false) {
    const value = this.keyResultForm.value;
    let keyResult: KeyResultDTO = {
      id: this.data.keyResult?.id,
      title: value.title,
      description: value.description,
      objective: this.data.objective,
      owner: value.owner,
    } as KeyResultDTO;

    if (this.isMetricKeyResult) {
      keyResult = {
        ...keyResult,
        keyResultType: 'metric',
        unit: value.unit,
        baseline: value.baseline,
        stretchGoal: value.stretchGoal,
      } as KeyResultMetricDTO;
    } else {
      keyResult = {
        ...keyResult,
        keyResultType: 'ordinal',
        commitZone: value.commitZone,
        targetZone: value.targetZone,
        stretchZone: value.stretchZone,
      } as KeyResultOrdinalDTO;
    }

    if (this.data.objective) {
      keyResult.objective = this.data.objective;
    }
    this.keyResultService.saveKeyResult(keyResult).subscribe((returnValue) => {
      this.dialogRef.close({
        id: keyResult.id,
        closeState: CloseState.SAVED,
        openNew: openNewDialog,
      });
    });
  }

  openNew() {
    this.saveKeyResult(true);
  }

  deleteKeyResult() {
    this.dialog
      .open(ConfirmDialogComponent, {
        data: {
          title: 'Key Result',
        },
        width: '15em',
        height: 'auto',
      })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.keyResultService
            .deleteKeyResult(this.data.keyResult.id)
            .subscribe(() => this.dialogRef.close({ closeState: CloseState.DELETED }));
        }
      });
  }

  isTouchedOrDirty(name: string) {
    return this.keyResultForm.get(name)?.dirty || this.keyResultForm.get(name)?.touched;
  }

  getErrorKeysOfFormField(name: string) {
    const errors = this.keyResultForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }

  saveEmittedData(keyResult: KeyResultEmitDTO) {
    this.isMetricKeyResult = keyResult.keyresultType == 'metric';
    if (keyResult.keyresultType == 'metric') {
      let metricKeyResult = keyResult as KeyResultEmitMetricDTO;
      this.keyResultForm.patchValue({
        baseline: metricKeyResult.baseline,
        stretchGoal: metricKeyResult.stretchGoal,
        unit: metricKeyResult.unit,
      });
    } else {
      let ordinalKeyResult = keyResult as KeyResultEmitOrdinalDTO;
      this.keyResultForm.patchValue({
        commitZone: ordinalKeyResult.commitZone,
        targetZone: ordinalKeyResult.targetZone,
        stretchZone: ordinalKeyResult.stretchZone,
      });
    }
  }

  filter(value: string): Observable<User[]> {
    const filterValue = value.toLowerCase();
    return this.users$.pipe(
      map((users) =>
        users.filter(
          (user) =>
            user.firstname.toLowerCase().includes(filterValue) ||
            user.lastname.toLowerCase().includes(filterValue) ||
            user.username.toLowerCase().includes(filterValue),
        ),
      ),
    );
  }

  getUserNameById(user: User): string {
    return user ? user.firstname + ' ' + user.lastname : '';
  }

  validOwner() {
    return typeof this.keyResultForm.value.owner === 'string';
  }
}
