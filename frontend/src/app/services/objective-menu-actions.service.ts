import { Injectable } from '@angular/core';
import { DialogService } from './dialog.service';
import { MatDialogRef } from '@angular/material/dialog';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { State } from '../shared/types/enums/State';
import { ObjectiveMenuAfterActions } from '../components/objective/ObjectiveMenuAfterActions';
import { ObjectiveService } from './objective.service';
import { RefreshDataService } from './refresh-data.service';
import { Objective } from '../shared/types/model/Objective';
import { ObjectiveMenuActions } from '../components/objective/ObjectiveMenuActions';
import { GJ_REGEX_PATTERN } from '../shared/constantLibary';

export type ObjectiveMenuAction = () => MatDialogRef<any>;
export type ObjectiveMenuAfterAction = (objective: Objective, dialogResult: any) => any;

export interface ObjectiveMenuEntry {
  displayName: string;
  action: ObjectiveMenuAction;
  afterAction: ObjectiveMenuAfterAction;
}

@Injectable({
  providedIn: 'root',
})
export class ObjectiveMenuActionsService {
  afterActions: ObjectiveMenuAfterActions;
  actions: ObjectiveMenuActions;
  constructor(
    dialogService: DialogService,
    objectiveService: ObjectiveService,
    refreshDataService: RefreshDataService,
  ) {
    this.afterActions = new ObjectiveMenuAfterActions(objectiveService, refreshDataService);
    this.actions = new ObjectiveMenuActions(dialogService, refreshDataService, this.afterActions);
  }

  getMenu(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [...this.getDefaultActions(objective), ...this.getSpecificMenuEntries(objective)];
  }

  private getSpecificMenuEntries(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    if (this.isObjectiveComplete(objective)) {
      return this.getCompletedMenuActions(objective);
    } else if (objective.state === State.ONGOING) {
      return this.getOngoingMenuActions(objective);
    } else if (objective.state === State.DRAFT) {
      return this.getDraftMenuActions(objective);
    }
    //Probably throw an error here
    return [];
  }

  private getDefaultActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [this.actions.duplicateObjectiveAction(objective)];
  }

  private getDraftMenuActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [this.getReleaseAction(objective)];
  }

  private getOngoingMenuActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [
      this.actions.editObjectiveAction(objective),
      this.actions.completeObjectiveAction(objective),
      this.actions.objectiveBackToDraft(),
    ];
  }

  private getCompletedMenuActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [this.actions.objectiveReopen()];
  }

  private getReleaseAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    return this.isInBacklogQuarter(objective)
      ? this.actions.releaseFromBacklogAction(objective)
      : this.actions.releaseFromQuarterAction(objective);
  }

  private isObjectiveComplete(objective: ObjectiveMin): boolean {
    return objective.state == State.SUCCESSFUL || objective.state == State.NOTSUCCESSFUL;
  }

  private isInBacklogQuarter(objective: ObjectiveMin) {
    return !GJ_REGEX_PATTERN.test(objective.quarter.label);
  }
}
