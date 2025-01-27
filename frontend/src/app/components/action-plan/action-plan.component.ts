import { AfterContentInit, Component, ElementRef, Input, QueryList, ViewChildren } from '@angular/core';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { Action } from '../../shared/types/model/action';
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
import { Observable, ReplaySubject } from 'rxjs';

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

  @Input() addItemSubject: ReplaySubject<Item | undefined> = new ReplaySubject<Item | undefined>();

  constructor(public dialogService: DialogService, private parentF: FormGroupDirective, private formArrayNameF: FormArrayName) {
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
    const item = this.getFormControlArray()
      .at(index)
      .getRawValue() as Item;

    if (item.item !== '' || item.id) {
      this.dialogService
        .openConfirmDialog('CONFIRMATION.DELETE.ACTION')
        .afterClosed()
        .subscribe((result) => {
          if (result) {
            this.getFormControlArray()
              .removeAt(index);
            if (item.id) {
              this.onDelete(item.id)
                .subscribe();
            }
          }
        });
    } else {
      this.getFormControlArray()
        .removeAt(index);
    }
  }

  addNewItem(item?: Item) {
    const newFormGroup = new FormGroup({
      item: new FormControl<string>(item?.item || ''),
      id: new FormControl<number | undefined>(item?.id || undefined),
      isChecked: new FormControl<boolean>(item?.isChecked || false)
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
    this.addItemSubject.subscribe((item) => this.addNewItem(item));
  }
}
