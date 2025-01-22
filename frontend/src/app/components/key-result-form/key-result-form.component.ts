import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { getFullNameOfUser, User } from '../../shared/types/model/user';
import { KeyResult } from '../../shared/types/model/key-result';
import { KeyResultMetric } from '../../shared/types/model/key-result-metric';
import { KeyResultOrdinal } from '../../shared/types/model/key-result-ordinal';
import { BehaviorSubject, filter, map, Observable, of, startWith, switchMap } from 'rxjs';
import { UserService } from '../../services/user.service';
import { Action } from '../../shared/types/model/action';
import { formInputCheck, hasFormFieldErrors } from '../../shared/common';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-key-result-form',
  templateUrl: './key-result-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class KeyResultFormComponent implements OnInit {
  users$ = new Observable<User[]>();

  filteredUsers$: Observable<User[]> = of([]);

  actionList$: BehaviorSubject<Action[] | null> = new BehaviorSubject<Action[] | null>([] as Action[]);

  protected readonly formInputCheck = formInputCheck;

  protected readonly hasFormFieldErrors = hasFormFieldErrors;


  @Input()
  keyResultForm!: FormGroup;

  @Input()
  keyResult?: KeyResult;

  constructor(public userService: UserService,
    private translate: TranslateService) {
  }

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
    this.filteredUsers$ = this.keyResultForm.get('owner')?.valueChanges.pipe(startWith(''), filter((value) => typeof value === 'string'), switchMap((value) => this.filter(value))) || of([]);
    if (this.keyResult) {
      this.keyResultForm.patchValue({ ...this.keyResult });

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
        keyResultId: undefined,
        isChecked: false },
      { id: null,
        version: 1,
        action: '',
        priority: 1,
        keyResultId: undefined,
        isChecked: false },
      { id: null,
        version: 1,
        action: '',
        priority: 2,
        keyResultId: undefined,
        isChecked: false }]);
    }

    this.actionList$.subscribe((value) => {
      this.keyResultForm.patchValue({ actionList: value });
    });
  }

  isMetricKeyResult() {
    return this.keyResultForm.controls['keyResultType'].value === 'metric';
  }

  setMetricValuesInForm(keyResultMetric: KeyResultMetric) {
    this.keyResultForm.get('metric')
      ?.patchValue({ ...keyResultMetric });
  }

  setOrdinalValuesInForm(keyResultOrdinal: KeyResultOrdinal) {
    this.keyResultForm.get('ordinal')
      ?.patchValue({ ...keyResultOrdinal });
  }

  filter(value: string): Observable<User[]> {
    const filterValue = value.toLowerCase();
    return this.users$.pipe(map((users) => users.filter((user) => getFullNameOfUser(user)
      .toLowerCase()
      .includes(filterValue))));
  }

  getFullNameOfLoggedInUser() {
    return getFullNameOfUser(this.userService.getCurrentUser());
  }
}
