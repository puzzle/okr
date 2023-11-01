import { Component, Input } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Action } from '../shared/types/model/Action';
import { ActionService } from '../shared/services/action.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../shared/dialog/confirm-dialog/confirm-dialog.component';
import { CONFIRM_DIALOG_WIDTH } from '../shared/constantLibary';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-action-plan',
  templateUrl: './action-plan.component.html',
  styleUrls: ['./action-plan.component.scss'],
})
export class ActionPlanComponent {
  @Input() control!: BehaviorSubject<Action[] | null>;
  @Input() keyResultId!: number | null;

  constructor(
    private actionService: ActionService,
    public dialog: MatDialog,
  ) {}

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
  }

  changeActionText(event: any, index: number) {
    const actions = this.control.getValue()!;
    actions[index] = { ...actions[index], action: event.target.value! };
    this.control.next(actions);
  }

  addNewAction() {
    const actions = this.control.getValue()!;
    actions.push({ action: '', priority: actions.length, keyResultId: this.keyResultId } as Action);
    this.control.next(actions);
  }

  removeAction(index: number) {
    const actions = this.control.getValue()!;
    if (actions[index].action !== '' || actions[index].id) {
      this.dialog
        .open(ConfirmDialogComponent, {
          data: {
            title: 'Action',
            isAction: true,
          },
          width: CONFIRM_DIALOG_WIDTH,
          height: 'auto',
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

  adjustPriorities() {
    const actions = this.control.getValue()!;
    actions.forEach(function (action, index) {
      action.priority = index;
    });
    this.control.next(actions);
  }
}
