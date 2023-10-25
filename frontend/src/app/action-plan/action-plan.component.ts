import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { ActionService } from '../shared/services/action.service';
import { Action } from '../shared/types/model/Action';

@Component({
  selector: 'app-action-plan',
  templateUrl: './action-plan.component.html',
  styleUrls: ['./action-plan.component.scss'],
})
export class ActionPlanComponent implements OnInit {
  @Input() keyResultId!: number | undefined;
  @Output() actionPlanEmitter = new EventEmitter<Action[]>();
  actionPointsText: string[] = [''];

  constructor(private actionService: ActionService) {}

  ngOnInit() {
    if (this.keyResultId) {
      this.actionService.getActionsFromKeyResult(this.keyResultId).subscribe((actions) => {
        this.actionPointsText = actions.map((action) => action.action);
      });
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data, event.container.data, event.previousIndex, event.currentIndex);
    }
    this.emitActionPlan();
  }

  changeActionText(event: any, index: number) {
    this.actionPointsText[index] = event.target.value!;
    this.emitActionPlan();
  }

  addNewAction() {
    this.actionPointsText.push('');
  }

  emitActionPlan() {
    let actionPoints: Action[] = [];
    for (let i = 0; i < this.actionPointsText.length; i++) {
      let newAction: Action = {
        id: null,
        action: this.actionPointsText[i],
        priority: i + 1,
        isChecked: false,
      };
      actionPoints.push(newAction);
    }
    this.actionPlanEmitter.emit(actionPoints);
  }
}
