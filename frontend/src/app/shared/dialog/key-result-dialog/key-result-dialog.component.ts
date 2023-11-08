import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from '../../types/model/User';
import { KeyResult } from '../../types/model/KeyResult';
import { KeyresultService } from '../../services/keyresult.service';
import { KeyResultMetricDTO } from '../../types/DTOs/KeyResultMetricDTO';
import errorMessages from '../../../../assets/errors/error-messages.json';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { Objective } from '../../types/model/Objective';
import { KeyResultOrdinalDTO } from '../../types/DTOs/KeyResultOrdinalDTO';
import { KeyResultMetric } from '../../types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../../types/model/KeyResultOrdinal';
import { BehaviorSubject, filter, map, Observable, of, startWith, switchMap } from 'rxjs';
import { UserService } from '../../services/user.service';
import { CloseState } from '../../types/enums/CloseState';
import { Action } from '../../types/model/Action';
import { CONFIRM_DIALOG_WIDTH } from '../../constantLibary';
import { formInputCheck } from '../../common';
import { OAuthService } from 'angular-oauth2-oidc';
import { keyResult } from '../../testData';

@Component({
  selector: 'app-key-result-dialog',
  templateUrl: './key-result-dialog.component.html',
  styleUrls: ['./key-result-dialog.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyResultDialogComponent implements OnInit {
  users$!: Observable<User[]>;
  filteredUsers$: Observable<User[]> | undefined = of([]);
  actionList$: BehaviorSubject<Action[] | null> = new BehaviorSubject<Action[] | null>([] as Action[]);
  protected readonly formInputCheck = formInputCheck;

  keyResultForm = new FormGroup({
    title: new FormControl<string>('', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    owner: new FormControl<User | string | null>(null, [Validators.required, Validators.nullValidator]),
    actionList: new FormControl<Action[]>([]),
    unit: new FormControl<string | null>(null),
    baseline: new FormControl<number | null>(null),
    stretchGoal: new FormControl<number | null>(null),
    commitZone: new FormControl<string | null>(null),
    targetZone: new FormControl<string | null>(null),
    stretchZone: new FormControl<string | null>(null),
    keyResultType: new FormControl<string>('metric'),
  });
  protected readonly errorMessages: any = errorMessages;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { objective: Objective; keyResult: KeyResult },
    public dialogRef: MatDialogRef<KeyResultDialogComponent>,
    private keyResultService: KeyresultService,
    public dialog: MatDialog,
    public userService: UserService,
    private oauthService: OAuthService,
  ) {
    if (this.data.keyResult) {
      this.actionList$ = new BehaviorSubject<Action[] | null>(this.data.keyResult.actionList);
    } else {
      this.actionList$ = new BehaviorSubject<Action[] | null>([
        { id: null, version: 1, action: '', priority: 0, keyResultId: null, isChecked: false },
        { id: null, version: 1, action: '', priority: 1, keyResultId: null, isChecked: false },
        { id: null, version: 1, action: '', priority: 2, keyResultId: null, isChecked: false },
      ]);
    }
  }

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
    this.filteredUsers$ = this.keyResultForm.get('owner')?.valueChanges.pipe(
      startWith(''),
      filter((value) => typeof value === 'string'),
      switchMap((value) => this.filter(value as string)),
    );
    if (this.data.keyResult) {
      this.keyResultForm.patchValue({ actionList: this.data.keyResult.actionList });
      this.keyResultForm.controls.title.setValue(this.data.keyResult.title);
      this.keyResultForm.controls.description.setValue(this.data.keyResult.description);
      this.keyResultForm.controls.owner.setValue(this.data.keyResult.owner);
      this.keyResultForm.controls['keyResultType'].setValue(this.data.keyResult.keyResultType);
      this.isMetricKeyResult()
        ? this.setMetricValuesInForm(this.data.keyResult as KeyResultMetric)
        : this.setOrdinalValuesInForm(this.data.keyResult as KeyResultOrdinal);
    }
    if (!this.data.keyResult) {
      this.users$.subscribe((users) => {
        const loggedInUser = this.getUserName();
        users.forEach((user) => {
          if (user.firstname + ' ' + user.lastname === loggedInUser) {
            this.keyResultForm.controls.owner.setValue(user);
          }
        });
      });
    }
  }

  setMetricValuesInForm(keyResultMetric: KeyResultMetric) {
    this.keyResultForm.controls.unit.setValue(keyResultMetric.unit);
    this.keyResultForm.controls.baseline.setValue(keyResultMetric.baseline);
    this.keyResultForm.controls.stretchGoal.setValue(keyResultMetric.stretchGoal);
  }

  setOrdinalValuesInForm(keyResultOrdinal: KeyResultOrdinal) {
    this.keyResultForm.controls.commitZone.setValue(keyResultOrdinal.commitZone);
    this.keyResultForm.controls.targetZone.setValue(keyResultOrdinal.targetZone);
    this.keyResultForm.controls.stretchZone.setValue(keyResultOrdinal.stretchZone);
  }

  isMetricKeyResult() {
    return this.keyResultForm.controls['keyResultType'].value === 'metric';
  }

  saveKeyResult(openNewDialog = false) {
    const value = this.keyResultForm.value;
    let keyResult = this.isMetricKeyResult()
      ? ({ ...value, objective: this.data.objective } as KeyResultMetricDTO)
      : ({ ...value, objective: this.data.objective, id: this.data.keyResult?.id } as KeyResultOrdinalDTO);
    keyResult.id = this.data.keyResult?.id;
    keyResult.version = this.data.keyResult?.version;
    keyResult.actionList = this.actionList$.getValue()!.filter((action: Action) => action.action !== '');
    this.keyResultService.saveKeyResult(keyResult).subscribe((returnValue) => {
      this.dialogRef.close({
        id: keyResult.id,
        version: keyResult.version,
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
        width: CONFIRM_DIALOG_WIDTH,
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

  filter(value: string): Observable<User[]> {
    const filterValue = value.toLowerCase();
    return this.users$.pipe(
      map((users) =>
        users.filter(
          (user) =>
            (user.firstname.toLowerCase() + ' ' + user.lastname.toLowerCase()).includes(filterValue) ||
            user.username.toLowerCase().includes(filterValue),
        ),
      ),
    );
  }

  getUserNameById(user: User): string {
    return user ? user.firstname + ' ' + user.lastname : '';
  }

  invalidOwner(): boolean {
    return (
      !!this.isTouchedOrDirty('owner') &&
      (typeof this.keyResultForm.value.owner === 'string' || !this.keyResultForm.value.owner)
    );
  }

  getKeyResultId(): number | null {
    return this.data.keyResult ? this.data.keyResult.id : null;
  }

  updateFormValidity() {}

  getUserName() {
    return this.oauthService.getIdentityClaims()['name'];
  }
}
