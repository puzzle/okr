import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  Input, OnDestroy,
  QueryList,
  ViewChildren
} from '@angular/core';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { DialogService } from '../../services/dialog.service';
import {
  AbstractControl,
  ControlContainer,
  FormArray,
  FormArrayName, FormControl,
  FormGroup, Validators
} from '@angular/forms';

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
export class ActionPlanComponent implements OnDestroy {
  @Input() canDelete?: boolean = true;

  @Input() movable = true;

  @Input() movable = true;

  @ViewChildren('listItem')
  listItems!: QueryList<ElementRef>;

  constructor(public dialogService: DialogService, private formArrayNameF: FormArrayName, private cdRef: ChangeDetectorRef) {
  }

  changeItemPosition(currentIndex: number, newIndex: number) {
    if (!this.movable) {
      return;
    }
    const value = this.getFormControlArray().value as Item[];
    moveItemInArray(value, currentIndex, newIndex);
    this.getFormControlArray()
      .setValue(value);
    this.focusItem(newIndex);
  }

  drop(event: CdkDragDrop<null>) {
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

    if ((item.item !== '' || item.id) && this.canDelete) {
      this.dialogService
        .openConfirmDialog('CONFIRMATION.DELETE.ACTION')
        .afterClosed()
        .subscribe((result) => {
          if (result) {
            this.getFormControlArray()
              .removeAt(index);
            this.cdRef.detectChanges();
          }
        });
    } else {
      this.getFormControlArray()
        .removeAt(index);
    }
  }

  addNewItem(item?: Item, options: { emitEvent?: boolean } = {}) {
    this.getFormControlArray()
      ?.push(this.initFormGroupFromItem(item), options);
  }

  /*
   * By default angular material adds a new entry inside the actionplan when the user presses enter
   *  to disable this behaviour we need this method which prevents the event from firing
   */
  preventAddingNewItems(event: Event) {
    event.preventDefault();
  }

  getFormControlArray() {
    return this.formArrayNameF.control as FormArray<FormGroup<FormControlsOf<Item>>>;
  }

  ngOnDestroy(): void {
    const validItems = this.getFormControlArray()
      .getRawValue()
      .filter((e) => e.item.trim() !== '');
    this.getFormControlArray()
      .clear({ emitEvent: false });
    validItems.forEach((item) => this.addNewItem(item, { emitEvent: false }));
  }

  initFormGroupFromItem(item?: Item): FormGroup<FormControlsOf<Item>> {
    return new FormGroup({
      item: new FormControl<string>(item?.item || '', [Validators.minLength(2)]),
      id: new FormControl<number | undefined>(item?.id || undefined),
      isChecked: new FormControl<boolean>(item?.isChecked || false)
    } as FormControlsOf<Item>);
  }
}
