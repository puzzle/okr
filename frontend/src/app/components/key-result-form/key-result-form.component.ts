import { AfterContentInit, ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { getFullNameOfUser, User } from '../../shared/types/model/user';
import { KeyResult } from '../../shared/types/model/key-result';
import { KeyResultMetric } from '../../shared/types/model/key-result-metric';
import { KeyResultOrdinal } from '../../shared/types/model/key-result-ordinal';
import { filter, map, Observable, of, startWith, switchMap } from 'rxjs';
import { UserService } from '../../services/user.service';
import { actionListToItemList, formInputCheck } from '../../shared/common';
import { ActionService } from '../../services/action.service';
import { FormControlsOf, Item } from '../action-plan/action-plan.component';

@Component({
  selector: 'app-key-result-form',
  templateUrl: './key-result-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class KeyResultFormComponent implements OnInit, AfterContentInit {
  users$ = new Observable<User[]>();

  filteredUsers$: Observable<User[]> = of([]);

  protected readonly formInputCheck = formInputCheck;

  @Input()
  keyResultForm!: FormGroup;

  @Input()
  keyResult?: KeyResult;

  constructor(public userService: UserService, public actionService: ActionService) {
  }

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
    this.filteredUsers$ = this.keyResultForm.get('owner')?.valueChanges.pipe(startWith(''), filter((value) => typeof value === 'string'), switchMap((value) => this.filter(value))) || of([]);
    if (this.keyResult) {
      this.keyResultForm.patchValue({ ...this.keyResult });

      this.isMetricKeyResult()
        ? this.setMetricValuesInForm(this.keyResult as KeyResultMetric)
        : this.setOrdinalValuesInForm(this.keyResult as KeyResultOrdinal);
    }
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

  addNewItem(item?: Item) {
    (this.keyResultForm.get('actionList') as FormArray)
      ?.push(this.initFormGroupFromItem(item));
  }

  ngAfterContentInit(): void {
    if (this.keyResult) {
      actionListToItemList(this.keyResult.actionList)
        .forEach((e) => {
          this.addNewItem(e);
        });
    } else {
      this.addNewItem();
      this.addNewItem();
      this.addNewItem();
    }
  }

  initFormGroupFromItem(item?: Item): FormGroup<FormControlsOf<Item>> {
    return new FormGroup({
      item: new FormControl<string>(item?.item || '', [Validators.minLength(2)]),
      id: new FormControl<number | undefined>(item?.id || undefined),
      isChecked: new FormControl<boolean>(item?.isChecked || false)
    } as FormControlsOf<Item>);
  }
}
