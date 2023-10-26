import { Component, Input } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Action } from '../shared/types/model/Action';
import { FormControl } from '@angular/forms';
import { ActionService } from '../shared/services/action.service';

@Component({
  selector: 'app-action-plan',
  templateUrl: './action-plan.component.html',
  styleUrls: ['./action-plan.component.scss'],
})
export class ActionPlanComponent {
  @Input() formControl!: FormControl<Action[] | null>;

  constructor(private actionService: ActionService) {}

  drop(event: CdkDragDrop<Action[] | null>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data!, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data!, event.container.data!, event.previousIndex, event.currentIndex);
    }
  }

  changeActionText(event: any, index: number) {
    const actions = this.formControl.value!;
    actions[index] = { ...actions[index], action: event.target.value! };
    this.formControl.setValue(actions);
  }

  addNewAction() {
    const actions = this.formControl.value!;
    actions.push({ action: '' } as Action);
    this.formControl.setValue(actions);
  }

  removeAction(index: number) {
    const actions = this.formControl.value!;
    this.actionService.deleteAction(actions[index].id!).subscribe();
    actions.splice(index, 1);
    this.formControl.setValue(actions);
  }
}
