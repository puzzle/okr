import { DialogService } from '../../services/dialog.service';
import { RefreshDataService } from '../../services/refresh-data.service';
import { ObjectiveMin } from '../../shared/types/model/objective-min';
import { ObjectiveFormComponent } from '../../shared/dialog/objective-dialog/objective-form.component';
import { CompleteDialogComponent } from '../../shared/dialog/complete-dialog/complete-dialog.component';
import {
  MenuAction,
  MenuAfterAction,
  ObjectiveMenuEntry
} from '../../services/objective-menu-actions.service';
import { ObjectiveMenuAfterActions } from './objective-menu-after-actions';

export class ObjectiveMenuActions {
  constructor(private readonly dialogService: DialogService,
    private readonly refreshDataService: RefreshDataService,
    private readonly afterActions: ObjectiveMenuAfterActions) {}

  releaseFromQuarterAction(): ObjectiveMenuEntry {
    const action: MenuAction = () => this.dialogService.openConfirmDialog('CONFIRMATION.RELEASE');
    const afterAction: MenuAfterAction<ObjectiveMin> = (objective: ObjectiveMin) => this.afterActions.releaseFromQuarter(objective);
    return { displayName: 'Objective veröffentlichen',
      action: action,
      afterAction: afterAction };
  }

  releaseFromBacklogAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = { data: { objective: { objectiveId: objective.id },
      action: 'releaseBacklog' } };
    const action: MenuAction = () => this.dialogService.open(ObjectiveFormComponent, config);
    const afterAction: MenuAfterAction<ObjectiveMin> = () => this.refreshDataService.markDataRefresh();
    return { displayName: 'Objective veröffentlichen',
      action: action,
      afterAction };
  }

  editObjectiveAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = { data: { objective: { objectiveId: objective.id } } };
    const action: MenuAction = () => this.dialogService.open(ObjectiveFormComponent, config);
    const afterAction: MenuAfterAction<ObjectiveMin> = () => {
      this.refreshDataService.markDataRefresh();
    };
    return { displayName: 'Objective bearbeiten',
      action: action,
      afterAction: afterAction };
  }

  duplicateObjectiveAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = { data: { objective: { objectiveId: objective.id },
      action: 'duplicate' } };
    const action: MenuAction = () => this.dialogService.open(ObjectiveFormComponent, config);
    const afterAction: MenuAfterAction<ObjectiveMin> = () => this.refreshDataService.markDataRefresh();
    return { displayName: 'Objective duplizieren',
      action: action,
      afterAction: afterAction };
  }

  deleteObjectiveAction(): ObjectiveMenuEntry {
    const action: MenuAction = () => this.dialogService.openConfirmDialog('CONFIRMATION.DELETE.OBJECTIVE');
    const afterAction: MenuAfterAction<ObjectiveMin> = (objective, dialogResult) => this.afterActions.deleteObjective(objective);
    return { displayName: 'Objective löschen',
      action: action,
      afterAction: afterAction };
  }

  completeObjectiveAction(objective: ObjectiveMin): ObjectiveMenuEntry {
    const config = {
      data: { objectiveTitle: objective.title }
    };
    const action: MenuAction = () => this.dialogService.open(CompleteDialogComponent, config);
    const afterAction: MenuAfterAction<ObjectiveMin> = (obj: ObjectiveMin, result: any) => this.afterActions.completeObjective(obj, result);

    return { displayName: 'Objective abschliessen',
      action: action,
      afterAction: afterAction };
  }

  objectiveBackToDraft(): ObjectiveMenuEntry {
    const action: MenuAction = () => this.dialogService.openConfirmDialog('CONFIRMATION.TO_DRAFT');
    const afterAction: MenuAfterAction<ObjectiveMin> = (obj: ObjectiveMin) => this.afterActions.objectiveBackToDraft(obj);

    return { displayName: 'Objective als Draft speichern',
      action: action,
      afterAction: afterAction };
  }

  objectiveReopen(): ObjectiveMenuEntry {
    const action: MenuAction = () => this.dialogService.openConfirmDialog('CONFIRMATION.REOPEN');
    const afterAction: MenuAfterAction<ObjectiveMin> = (obj: ObjectiveMin) => this.afterActions.objectiveReopen(obj);

    return { displayName: 'Objective wiedereröffnen',
      action: action,
      afterAction: afterAction };
  }
}
