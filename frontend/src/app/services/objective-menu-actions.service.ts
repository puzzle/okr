import { Injectable } from '@angular/core';
import { DialogService } from './dialog.service';
import { MatDialogRef } from '@angular/material/dialog';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { CompleteDialogComponent } from '../shared/dialog/complete-dialog/complete-dialog.component';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { State } from '../shared/types/enums/State';
import { isInBacklogQuarter, isObjectiveComplete } from '../shared/common';
import { ObjectiveMenuAfterActionFactory } from '../components/objective/ObjectiveMenuAfterActionFactory';
import { ObjectiveService } from './objective.service';
import { RefreshDataService } from './refresh-data.service';
import { Objective } from '../shared/types/model/Objective';

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
  afterActions: ObjectiveMenuAfterActionFactory;
  constructor(
    private readonly dialogService: DialogService,
    objectiveService: ObjectiveService,
    private readonly refreshDataService: RefreshDataService,
  ) {
    this.afterActions = new ObjectiveMenuAfterActionFactory(dialogService, objectiveService, refreshDataService);
  }

  getMenu(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [...this.getDefaultActions(objective), ...this.getSpecificMenuEntries(objective)];
  }

  private getSpecificMenuEntries(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    if (isObjectiveComplete(objective)) {
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
    return [this.duplicateObjectiveAction(objective)];
  }

  private getDraftMenuActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    const releaseAction = isInBacklogQuarter(objective)
      ? this.releaseFromBacklogAction(objective)
      : this.releaseFromQuarterAction(objective);
    return [releaseAction];
  }

  private getOngoingMenuActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [this.editObjectiveAction(objective), this.completeObjectiveAction(objective), this.objectiveBackToDraft()];
  }

  private getCompletedMenuActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [this.objectiveReopen()];
  }

  private releaseFromQuarterAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const action: ObjectiveMenuAction = () => this.dialogService.openConfirmDialog('CONFIRMATION.RELEASE');
    const afterAction: ObjectiveMenuAfterAction = (objective, dialogResult) =>
      this.afterActions.releaseFromQuarter(objective);
    return { displayName: 'Objective veröffentlichen', action: action, afterAction: afterAction };
  }

  private releaseFromBacklogAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = { data: { objective: { objectiveId: objective }, action: 'releaseBacklog' } };
    const action: ObjectiveMenuAction = () => this.dialogService.open(ObjectiveFormComponent, config);
    const afterAction: ObjectiveMenuAfterAction = () => this.refreshDataService.markDataRefresh();
    return { displayName: 'Objective veröffentlichen', action: action, afterAction };
  }

  private editObjectiveAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = { data: { objective: { objectiveId: objective.id } } };
    const action: ObjectiveMenuAction = () => this.dialogService.open(ObjectiveFormComponent, config);
    const afterAction: ObjectiveMenuAfterAction = () => {};
    return { displayName: 'Objective bearbeiten', action: action, afterAction: afterAction };
  }

  private duplicateObjectiveAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = { data: { objective: { objectiveId: objective.id } } };
    const action: ObjectiveMenuAction = () => this.dialogService.open(ObjectiveFormComponent, config);
    const afterAction: ObjectiveMenuAfterAction = () => this.refreshDataService.markDataRefresh();
    return { displayName: 'Objective duplizieren', action: action, afterAction: afterAction };
  }

  private completeObjectiveAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = {
      data: { objectiveTitle: objective.title },
    };
    const action: ObjectiveMenuAction = () => this.dialogService.open(CompleteDialogComponent, config);
    const afterAction: ObjectiveMenuAfterAction = (obj: Objective, result: any) =>
      this.afterActions.completeObjective(obj, result);

    return { displayName: 'Objective abschliessen', action: action, afterAction: afterAction };
  }

  private objectiveBackToDraft(): ObjectiveMenuEntry {
    const action: ObjectiveMenuAction = () => this.dialogService.openConfirmDialog('CONFIRMATION.DRAFT_CREATE');
    const afterAction: ObjectiveMenuAfterAction = (obj: Objective, result: any) =>
      this.afterActions.objectiveBackToDraft(obj);

    return { displayName: 'Objective als Draft speicherns', action: action, afterAction: afterAction };
  }

  private objectiveReopen(): ObjectiveMenuEntry {
    const action: ObjectiveMenuAction = () => this.dialogService.openConfirmDialog('CONFIRMATION.REOPEN');
    const afterAction: ObjectiveMenuAfterAction = (obj: Objective, result: any) =>
      this.afterActions.objectiveReopen(obj);

    return { displayName: 'Objective wiedereröffnen', action: action, afterAction: afterAction };
  }
}
