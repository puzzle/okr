import { AfterViewInit, Component, ElementRef, Input, QueryList, ViewChildren } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Action } from '../shared/types/model/Action';
import { ActionService } from '../shared/services/action.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../shared/dialog/confirm-dialog/confirm-dialog.component';
import { BehaviorSubject, Subscription } from 'rxjs';
import { isMobileDevice } from '../shared/common';
import { CONFIRM_DIALOG_WIDTH } from '../shared/constantLibary';

@Component({
  selector: 'app-action-plan',
  templateUrl: './action-plan.component.html',
  styleUrls: ['./action-plan.component.scss'],
})
export class ActionPlanComponent implements AfterViewInit {
  @Input() control: BehaviorSubject<Action[] | null> = new BehaviorSubject<Action[] | null>([]);
  @Input() keyResultId!: number | null;
  activeItem: number = 0;
  listSubscription!: Subscription;

  @ViewChildren('listItem')
  listItems!: QueryList<ElementRef>;

  constructor(
    private actionService: ActionService,
    public dialog: MatDialog,
  ) {}

  ngAfterViewInit() {
    this.listSubscription = this.listItems.changes.subscribe((_) => {
      if (this.listItems.length > 0) {
        this.listItems.toArray()[this.activeItem].nativeElement.focus();
      }
    });
  }

  handleKeyDown(event: Event, currentIndex: number) {
    let newIndex = currentIndex;
    if ((event as KeyboardEvent).key === 'ArrowDown') {
      if (newIndex + 1 <= this.control.getValue()!.length - 1) {
        newIndex += 1;
      }
    } else if ((event as KeyboardEvent).key === 'ArrowUp') {
      if (newIndex - 1 >= 0) {
        newIndex -= 1;
      }
    }
    this.changeItemPosition(newIndex, currentIndex);
    this.adjustPriorities();
  }

  changeItemPosition(newIndex: number, currentIndex: number) {
    moveItemInArray(this.control.getValue()!, currentIndex, newIndex);
    this.activeItem = newIndex;
  }

  increaseActiveItemWithTab() {
    if (this.activeItem <= this.control.value!.length - 2) {
      this.activeItem++;
    }
  }

  decreaseActiveItemWithShiftTab() {
    if (this.activeItem > 0) {
      this.activeItem--;
    }
  }

  drop(event: CdkDragDrop<Action[] | null>) {
    let value = (<HTMLInputElement>event.container.element.nativeElement.children[event.previousIndex].children[1])
      .value;
    const actions = this.control.getValue()!;
    if (actions[event.previousIndex].action == '' && value != '') {
      actions[event.previousIndex] = { ...actions[event.previousIndex], action: value };
      this.control.next(actions);
    }
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data!, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data!, event.container.data!, event.previousIndex, event.currentIndex);
    }
    this.adjustPriorities();
    this.activeItem = event.currentIndex;
  }

  changeActionText(event: any, index: number) {
    const actions = this.control.getValue()!;
    actions[index] = { ...actions[index], action: event.target.value! };
    this.control.next(actions);
  }

  adjustPriorities() {
    const actions = this.control.getValue()!;
    actions.forEach(function (action, index) {
      action.priority = index;
    });
    this.control.next(actions);
  }

  removeAction(index: number) {
    let actions = this.control.getValue()!;
    if (this.activeItem == index && this.activeItem > 0) {
      this.activeItem--;
    }
    if (actions[index].action !== '' || actions[index].id) {
      const dialogConfig = isMobileDevice()
        ? {
            maxWidth: '100vw',
            maxHeight: '100vh',
            height: '100vh',
            width: CONFIRM_DIALOG_WIDTH,
          }
        : {
            width: '45em',
            height: 'auto',
          };
      this.dialog
        .open(ConfirmDialogComponent, {
          data: {
            title: 'Action',
            isAction: true,
          },
          width: dialogConfig.width,
          height: dialogConfig.height,
          maxHeight: dialogConfig.maxHeight,
          maxWidth: dialogConfig.maxWidth,
        })
        .afterClosed()
        .subscribe((result) => {
          if (result) {
            if (actions[index].id) {
              this.actionService.deleteAction(actions[index].id!).subscribe();
            }
            actions.splice(index, 1);
            this.control.next(actions);
            this.adjustPriorities();
          }
        });
    } else {
      actions.splice(index, 1);
      this.control.next(actions);
      this.adjustPriorities();
    }
  }

  addNewAction() {
    const actions = this.control.getValue()!;
    actions.push({ action: '', priority: actions.length, keyResultId: this.keyResultId } as Action);
    this.control.next(actions);
    this.activeItem = actions.length - 1;
  }

  /* By default angular material adds a new entry inside the actionplan when the user presses enter
   *  to disable this behaviour we need this method which prevents the event from firing */
  preventAddingNewItems(event: Event) {
    event.preventDefault();
  }
}
