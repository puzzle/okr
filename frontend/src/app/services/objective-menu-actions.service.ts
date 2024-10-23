import { Injectable } from '@angular/core';
import { DialogService } from './dialog.service';
import { MatDialogRef } from '@angular/material/dialog';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { CompleteDialogComponent } from '../shared/dialog/complete-dialog/complete-dialog.component';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { State } from '../shared/types/enums/State';
import { isInBacklogQuarter, isObjectiveComplete } from '../shared/common';

export type ObjectiveMenuAction = () => MatDialogRef<any>;

export interface ObjectiveMenuEntry {
  displayName: string;
  action: ObjectiveMenuAction;
  afterAction: (arg?: any) => any;
}

@Injectable({
  providedIn: 'root',
})
export class ObjectiveMenuActionsService {
  constructor(private readonly dialogService: DialogService) {}

  getMenu(objective: ObjectiveMin): ObjectiveMenuEntry[] {
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

  private getOngoingMenuActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [
      ...this.getDefaultActions(objective),
      this.saveObjectiveAsDraftAction(),
      this.completeObjectiveAction(objective),
    ];
  }

  private getDefaultActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [this.editObjectiveAction(objective), this.duplicateObjectiveAction(objective)];
  }

  private getDraftMenuActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    const releaseAction = isInBacklogQuarter(objective)
      ? this.releaseFromDraftInBacklogAction(objective)
      : this.releaseFromDraftAction(objective);
    return [releaseAction];
  }

  private releaseFromDraftAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const action = () => this.dialogService.openConfirmDialog('CONFIRMATION.RELEASE');
    const afterAction = () => {};
    return { displayName: 'Objective veröffentlichen', action: action, afterAction: afterAction };
  }

  private releaseFromDraftInBacklogAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = { data: { objective: { objectiveId: objective }, action: 'releaseBacklog' } };
    const action: ObjectiveMenuAction = () => this.dialogService.open(ObjectiveFormComponent, config);
    const afterAction = () => {};
    return { displayName: 'Objective veröffentlichen', action: action, afterAction };
  }

  private editObjectiveAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = { data: { objective: { objectiveId: objective.id } } };
    const action: ObjectiveMenuAction = () => this.dialogService.open(ObjectiveFormComponent, config);
    const afterAction = () => {};
    return { displayName: 'Objective bearbeiten', action: action, afterAction: afterAction };
  }

  private duplicateObjectiveAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = { data: { objective: { objectiveId: objective.id } } };
    const action = () => this.dialogService.open(ObjectiveFormComponent, config);
    const afterAction = () => {};
    return { displayName: 'Objective duplizieren', action: action, afterAction: afterAction };
  }

  private completeObjectiveAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = {
      data: { objectiveTitle: objective.title },
    };
    const action = () => this.dialogService.open(CompleteDialogComponent, config);
    const afterAction = () => {};

    return { displayName: 'Objective abschliessen', action: action, afterAction: afterAction };
  }

  private saveObjectiveAsDraftAction(): ObjectiveMenuEntry {
    const action = () => this.dialogService.openConfirmDialog('CONFIRMATION.DRAFT_CREATE');
    const afterAction = () => {};

    return { displayName: 'Objective als Draft speicherns', action: action, afterAction: afterAction };
  }

  private getCompletedMenuActions(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    return [];
  }
}
