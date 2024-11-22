import { Component, ElementRef, Input, QueryList, ViewChildren } from "@angular/core";
import { CdkDragDrop, moveItemInArray, transferArrayItem } from "@angular/cdk/drag-drop";
import { Action } from "../../shared/types/model/Action";
import { ActionService } from "../../services/action.service";
import { BehaviorSubject } from "rxjs";
import { trackByFn } from "../../shared/common";
import { DialogService } from "../../services/dialog.service";

@Component({
  selector: "app-action-plan",
  templateUrl: "./action-plan.component.html",
  styleUrls: ["./action-plan.component.scss"],
})
export class ActionPlanComponent {
  @Input() control: BehaviorSubject<Action[] | null> = new BehaviorSubject<Action[] | null>([]);
  @Input() keyResultId!: number | null;
  activeItem: number = 0;

  @ViewChildren("listItem")
  listItems!: QueryList<ElementRef>;

  constructor(
    private actionService: ActionService,
    public dialogService: DialogService,
  ) {}

  handleKeyDown(event: Event, currentIndex: number) {
    let newIndex = currentIndex;
    if ((event as KeyboardEvent).key === "ArrowDown") {
      if (newIndex + 1 <= this.control.getValue()!.length - 1) {
        newIndex += 1;
      }
    } else if ((event as KeyboardEvent).key === "ArrowUp") {
      if (newIndex - 1 >= 0) {
        newIndex -= 1;
      }
    }
    this.changeItemPosition(newIndex, currentIndex);
    this.listItems.get(this.activeItem)?.nativeElement.focus();
  }

  changeItemPosition(newIndex: number, currentIndex: number) {
    this.activeItem = newIndex;
    const currentActionPlan: Action[] = this.control.getValue()!;
    this.updateActionTexts(currentActionPlan);
    moveItemInArray(currentActionPlan, currentIndex, newIndex);
    currentActionPlan.forEach((action: Action, index: number) => (action.priority = index));
    this.control.next(currentActionPlan);
  }

  updateActionTexts(currentActionPlan: Action[]) {
    const texts = Array.from(this.listItems)
      .map((input: any) => input.nativeElement.value);
    currentActionPlan.forEach((action: Action, index: number) => (action.action = texts[index]));
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
    const value: string = (<HTMLInputElement>(
      event.container.element.nativeElement.children[event.previousIndex].children[1]
    )).value;
    const actions: Action[] = this.control.getValue()!;
    if (actions[event.previousIndex].action == "" && value != "") {
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

  adjustPriorities() {
    const actions: Action[] = this.control.getValue()!;
    actions.forEach(function (action: Action, index: number) {
      action.priority = index;
    });
    this.control.next(actions);
  }

  removeAction(index: number) {
    const actions: Action[] = this.control.getValue()!;
    if (this.activeItem == index && this.activeItem > 0) {
      this.activeItem--;
    }
    if (actions[index].action !== "" || actions[index].id) {
      this.dialogService
        .openConfirmDialog("DELETE.ACTION")
        .afterClosed()
        .subscribe((result) => {
          if (result) {
            if (actions[index].id) {
              this.actionService.deleteAction(actions[index].id!)
                .subscribe();
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
    const actions: Action[] = this.control.getValue()!;
    actions.push({ action: "", priority: actions.length, keyResultId: this.keyResultId } as Action);
    this.control.next(actions);
    this.activeItem = actions.length - 1;
  }

  /* By default angular material adds a new entry inside the actionplan when the user presses enter
   *  to disable this behaviour we need this method which prevents the event from firing */
  preventAddingNewItems(event: Event) {
    event.preventDefault();
  }

  protected readonly trackByFn = trackByFn;
}
