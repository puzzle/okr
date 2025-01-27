import { AfterContentInit, Component, ElementRef, Input, QueryList, ViewChildren } from '@angular/core';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { Action } from '../../shared/types/model/action';
import { ActionService } from '../../services/action.service';
import { trackByFn } from '../../shared/common';
import { DialogService } from '../../services/dialog.service';
import {
  AbstractControl,
  ControlContainer,
  FormArray,
  FormArrayName,
  FormControl,
  FormGroup,
  FormGroupDirective
} from '@angular/forms';
import { Observable } from 'rxjs';

export type FormControlsOf<T> = {
  [P in keyof T]: AbstractControl<T[P]>;
};

export interface Item {
  id: number | undefined;
  item: string;
  isChecked: boolean;
}

@Component({
  selector: 'app-action-plan',
  templateUrl: './action-plan.component.html',
  styleUrls: ['./action-plan.component.scss'],
  standalone: false,
  viewProviders: [{ provide: ControlContainer,
    useExisting: FormArrayName }]
})
export class ActionPlanComponent implements AfterContentInit {
  form?: FormGroup;

  @Input() onDelete: (index: number) => Observable<any> = () => new Observable();

  @ViewChildren('listItem')
  listItems!: QueryList<ElementRef>;

  constructor(
    private actionService: ActionService,
    public dialogService: DialogService, private parentF: FormGroupDirective, private formArrayNameF: FormArrayName
  ) {
  }

  changeItemPosition(currentIndex: number, newIndex: number) {
    const value = this.getFormControlArray().value as Item[];
    moveItemInArray(value, currentIndex, newIndex);
    this.getFormControlArray()
      .setValue(value);
    this.focusItem(newIndex);
  }

  drop(event: CdkDragDrop<Action[] | null>) {
    this.changeItemPosition(event.previousIndex, event.currentIndex);
  }

  focusItem(index: number) {
    this.listItems.get(index)?.nativeElement.focus();
    this.listItems.get(index)?.nativeElement.scrollIntoView();
  }

  removeAction(index: number) {
    const itemGroup = this.getFormControlArray()
      .at(index) as FormGroup;
    const itemValue = itemGroup.get('item')?.value;
    const itemId = itemGroup.get('id')?.value;

    if (itemValue !== '' || itemId) {
      this.dialogService
        .openConfirmDialog('CONFIRMATION.DELETE.ACTION')
        .afterClosed()
        .subscribe((result) => {
          if (result) {
            this.getFormControlArray()
              .removeAt(index);

            this.onDelete(itemId)
              .subscribe();
          }
        });
    } else {
      this.getFormControlArray()
        .removeAt(index);
    }
  }

  addNewItem() {
    const newFormGroup = new FormGroup({
      item: new FormControl<string>(''),
      id: new FormControl<number | undefined>(undefined),
      isChecked: new FormControl<boolean>(false)
    } as FormControlsOf<Item>);
    this.getFormControlArray()
      ?.push(newFormGroup);
  }

  /*
   * By default angular material adds a new entry inside the actionplan when the user presses enter
   *  to disable this behaviour we need this method which prevents the event from firing
   */
  preventAddingNewItems(event: Event) {
    event.preventDefault();
  }

  protected readonly trackByFn = trackByFn;

  getFormControlArray() {
    return this.form?.get(`${this.formArrayNameF.name}`) as FormArray<FormGroup<FormControlsOf<Item>>>;
  }

  ngAfterContentInit(): void {
    this.form = this.parentF.form;
    if (this.getFormControlArray()
      ?.getRawValue().length === 0) {
      this.addNewItem();
      this.addNewItem();
      this.addNewItem();
    }
  }
}
