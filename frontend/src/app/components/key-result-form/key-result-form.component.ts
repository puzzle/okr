import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { getFullNameOfUser, User } from '../../shared/types/model/user';
import { KeyResult } from '../../shared/types/model/key-result';
import { KeyResultMetric } from '../../shared/types/model/key-result-metric';
import { KeyResultOrdinal } from '../../shared/types/model/key-result-ordinal';
import { filter, map, Observable, of, startWith, switchMap } from 'rxjs';
import { UserService } from '../../services/user.service';
import { actionListToItemList, formInputCheck, hasFormFieldErrors } from '../../shared/common';
import { TranslateService } from '@ngx-translate/core';
import { ActionService } from '../../services/action.service';
import { FormControlsOf, Item } from '../action-plan/action-plan.component';

@Component({
  selector: 'app-key-result-form',
  templateUrl: './key-result-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class KeyResultFormComponent implements OnInit {
  users$ = new Observable<User[]>();

  filteredUsers$: Observable<User[]> = of([]);

  actionPlanOnDelete = (index: number): Observable<any> => this.actionService.deleteAction(index);

  protected readonly formInputCheck = formInputCheck;

  protected readonly hasFormFieldErrors = hasFormFieldErrors;


  @Input()
  keyResultForm!: FormGroup;

  @Input()
  keyResult?: KeyResult;

  constructor(public userService: UserService,
    private translate: TranslateService, public actionService: ActionService) {
  }

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
    this.filteredUsers$ = this.keyResultForm.get('owner')?.valueChanges.pipe(startWith(''), filter((value) => typeof value === 'string'), switchMap((value) => this.filter(value))) || of([]);
    if (this.keyResult) {
      this.keyResultForm.patchValue({ ...this.keyResult });

      this.isMetricKeyResult()
        ? this.setMetricValuesInForm(this.keyResult as KeyResultMetric)
        : this.setOrdinalValuesInForm(this.keyResult as KeyResultOrdinal);
      actionListToItemList(this.keyResult.actionList)
        .forEach((e) => {
          this.addNewItem(e);
        });
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

  addNewItem(item: Item) {
    const newFormGroup = new FormGroup({
      item: new FormControl<string>(item.item),
      id: new FormControl<number | undefined>(item.id),
      isChecked: new FormControl<boolean>(item.isChecked)
    } as FormControlsOf<Item>);
    (this.keyResultForm.get('actionList') as FormArray)?.push(newFormGroup);
  }
}
