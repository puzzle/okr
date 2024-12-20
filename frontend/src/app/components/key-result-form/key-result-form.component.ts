import { ChangeDetectionStrategy, Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { getFullNameOfUser, User } from '../../shared/types/model/User';
import { KeyResult } from '../../shared/types/model/KeyResult';
import { KeyResultMetric } from '../../shared/types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../../shared/types/model/KeyResultOrdinal';
import { BehaviorSubject, filter, map, Observable, of, startWith, Subject, switchMap, takeUntil } from 'rxjs';
import { UserService } from '../../services/user.service';
import { Action } from '../../shared/types/model/Action';
import { formInputCheck, hasFormFieldErrors } from '../../shared/common';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-key-result-form',
  templateUrl: './key-result-form.component.html',
  styleUrls: ['./key-result-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class KeyResultFormComponent implements OnInit, OnDestroy {
  users$!: Observable<User[]>;

  filteredUsers$: Observable<User[]> | undefined = of([]);

  actionList$: BehaviorSubject<Action[] | null> = new BehaviorSubject<Action[] | null>([] as Action[]);

  protected readonly formInputCheck = formInputCheck;

  protected readonly hasFormFieldErrors = hasFormFieldErrors;

  private unsubscribe$ = new Subject<void>();

  @Input()
  keyResultForm!: FormGroup;

  @Input()
  keyResult!: KeyResult | null;

  constructor(public userService: UserService,
    private translate: TranslateService) {}

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
    this.filteredUsers$ = this.keyResultForm.get('owner')?.valueChanges.pipe(startWith(''), filter((value) => typeof value === 'string'), switchMap((value) => this.filter(value as string)));
    if (this.keyResult) {
      this.keyResultForm.patchValue({ actionList: this.keyResult.actionList });
      this.keyResultForm.controls['title'].setValue(this.keyResult.title);
      this.keyResultForm.controls['description'].setValue(this.keyResult.description);
      this.keyResultForm.controls['owner'].setValue(this.keyResult.owner);
      this.keyResultForm.controls['keyResultType'].setValue(this.keyResult.keyResultType);
      this.isMetricKeyResult()
        ? this.setMetricValuesInForm(this.keyResult as KeyResultMetric)
        : this.setOrdinalValuesInForm(this.keyResult as KeyResultOrdinal);

      this.actionList$ = new BehaviorSubject<Action[] | null>(this.keyResult.actionList);
    }
    if (!this.keyResult) {
      this.actionList$ = new BehaviorSubject<Action[] | null>([{ id: null,
        version: 1,
        action: '',
        priority: 0,
        keyResultId: null,
        isChecked: false },
      { id: null,
        version: 1,
        action: '',
        priority: 1,
        keyResultId: null,
        isChecked: false },
      { id: null,
        version: 1,
        action: '',
        priority: 2,
        keyResultId: null,
        isChecked: false }]);

      this.users$.pipe(takeUntil(this.unsubscribe$))
        .subscribe((users) => {
          const loggedInUser = this.getFullNameOfLoggedInUser();
          users.forEach((user) => {
            if (getFullNameOfUser(user) === loggedInUser) {
              this.keyResultForm.controls['owner'].setValue(user);
            }
          });
        });
    }

    this.actionList$.subscribe((value) => {
      this.keyResultForm.patchValue({ actionList: value });
    });
  }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  isMetricKeyResult() {
    return this.keyResultForm.controls['keyResultType'].value === 'metric';
  }

  setMetricValuesInForm(keyResultMetric: KeyResultMetric) {
    this.keyResultForm.controls['unit'].setValue(keyResultMetric.unit);
    this.keyResultForm.controls['baseline'].setValue(keyResultMetric.baseline);
    this.keyResultForm.controls['stretchGoal'].setValue(keyResultMetric.stretchGoal);
  }

  setOrdinalValuesInForm(keyResultOrdinal: KeyResultOrdinal) {
    this.keyResultForm.controls['commitZone'].setValue(keyResultOrdinal.commitZone);
    this.keyResultForm.controls['targetZone'].setValue(keyResultOrdinal.targetZone);
    this.keyResultForm.controls['stretchZone'].setValue(keyResultOrdinal.stretchZone);
  }

  isTouchedOrDirty(name: string) {
    return this.keyResultForm.get(name)?.dirty || this.keyResultForm.get(name)?.touched;
  }

  getErrorMessage(
    error: string, field: string, firstNumber: number | null, secondNumber: number | null
  ): string {
    return field + this.translate.instant('DIALOG_ERRORS.' + error)
      .format(firstNumber, secondNumber);
  }

  filter(value: string): Observable<User[]> {
    const filterValue = value.toLowerCase();
    return this.users$.pipe(map((users) => users.filter((user) => getFullNameOfUser(user)
      .toLowerCase()
      .includes(filterValue))));
  }

  invalidOwner(): boolean {
    return (
      !!this.isTouchedOrDirty('owner') &&
      (typeof this.keyResultForm.value.owner === 'string' || !this.keyResultForm.value.owner)
    );
  }

  getFullNameOfUser(user: User): string {
    return user ? getFullNameOfUser(user) : '';
  }

  getKeyResultId(): number | null {
    return this.keyResult ? this.keyResult.id : null;
  }

  updateFormValidity() {}

  getFullNameOfLoggedInUser() {
    return this.getFullNameOfUser(this.userService.getCurrentUser());
  }
}
