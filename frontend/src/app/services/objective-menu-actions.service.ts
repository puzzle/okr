import { Injectable } from '@angular/core';
import { DialogService } from './dialog.service';
import { MatDialogRef } from '@angular/material/dialog';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { CompleteDialogComponent } from '../shared/dialog/complete-dialog/complete-dialog.component';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { GJ_REGEX_PATTERN } from '../shared/constantLibary';

export type ObjectiveMenuAction = {
  (): MatDialogRef<any>;
  (arg1: any): MatDialogRef<any>;
  (arg1: any, arg2: any): MatDialogRef<any>;
};

export interface ObjectiveMenuEntry {
  displayName: string;
  action: ObjectiveMenuAction;
}

@Injectable({
  providedIn: 'root',
})
export class ObjectiveMenuActionsService {
  constructor(private readonly dialogService: DialogService) {}

  isInBacklogQuarter(objective: ObjectiveMin) {
    return !GJ_REGEX_PATTERN.test(objective.quarter.label);
  }

  getOngoingMenuActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [
      ...this.getDefaultActions(objective),
      this.saveObjectiveAsDraftAction(),
      this.completeObjectiveAction(objective),
    ];
  }

  getDefaultActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [this.editObjectiveAction(objective), this.duplicateObjectiveAction(objective)];
  }

  getDraftMenuActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    const releaseAction = this.isInBacklogQuarter(objective)
      ? this.releaseFromDraftInBacklogAction(objective)
      : this.releaseFromDraftAction(objective);
    return [releaseAction];
  }

  releaseFromDraftAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const action = () => this.dialogService.openConfirmDialog('CONFIRMATION.RELEASE');
    return { displayName: 'Objective veröffentlichen', action: action };
  }

  releaseFromDraftInBacklogAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = { data: { action: 'releaseBacklog', objectiveId: objective } };
    const action = () => this.dialogService.open(ObjectiveFormComponent, config);
    return { displayName: 'Objective veröffentlichen', action: action };
  }

  private editObjectiveAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = { data: { objectiveId: objective.id } };
    const action: ObjectiveMenuAction = () => this.dialogService.open(ObjectiveFormComponent, config);
    return { displayName: 'Objective bearbeiten', action: action };
  }

  private duplicateObjectiveAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = { data: { objectiveId: objective.id } };
    const action = () => this.dialogService.open(ObjectiveFormComponent, config);
    return { displayName: 'Objective duplizieren', action: action };
  }

  private completeObjectiveAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = {
      data: { objectiveTitle: objective.title },
    };
    const action = () => this.dialogService.open(CompleteDialogComponent, config);
    return { displayName: 'Objective abschliessen', action: action };
  }

  private saveObjectiveAsDraftAction(): ObjectiveMenuEntry {
    const action = () => this.dialogService.openConfirmDialog('CONFIRMATION.DRAFT_CREATE');
    return { displayName: 'Objective als Draft speichern', action: action };
  }

  getCompletedMenuActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [];
  }
}
