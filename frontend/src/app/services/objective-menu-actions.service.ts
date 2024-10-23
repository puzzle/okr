import { Injectable } from '@angular/core';
import { DialogService } from './dialog.service';
import { MatDialogRef } from '@angular/material/dialog';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { CompleteDialogComponent } from '../shared/dialog/complete-dialog/complete-dialog.component';

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

  getOngoingMenuActions(objectiveId: number): ObjectiveMenuEntry[] {
    return [
      ...this.getDefaultActions(objectiveId),
      this.saveObjectiveAsDraftAction(),
      this.completeObjectiveAction(objectiveId),
    ];
  }

  getDefaultActions(objectiveId: number): ObjectiveMenuEntry[] {
    return [this.editObjectiveAction(objectiveId), this.duplicateObjectiveAction(objectiveId)];
  }

  private editObjectiveAction(objectiveId: number): ObjectiveMenuEntry {
    const config = { data: { objectiveId: objectiveId } };
    const action: ObjectiveMenuAction = () => this.dialogService.open(ObjectiveFormComponent, config);
    return { displayName: 'Objective bearbeiten', action: action };
  }

  private duplicateObjectiveAction(objectiveId: number): ObjectiveMenuEntry {
    const config = { data: { objectiveId: objectiveId } };
    const action = () => this.dialogService.open(ObjectiveFormComponent, config);
    return { displayName: 'Objective duplizieren', action: action };
  }

  private completeObjectiveAction(objectiveId: number): ObjectiveMenuEntry {
    const config = {
      data: { objectiveTitle: 'Objective Title Placeholder' },
    };
    const action = () => this.dialogService.open(CompleteDialogComponent, config);
    return { displayName: 'Objective abschliessen', action: action };
  }

  private saveObjectiveAsDraftAction(): ObjectiveMenuEntry {
    const action = () => this.dialogService.openConfirmDialog('CONFIRMATION.DRAFT_CREATE');
    return { displayName: 'Objective als Draft speichern', action: action };
  }
}
