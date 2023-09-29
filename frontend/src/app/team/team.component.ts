import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { MatDialog } from '@angular/material/dialog';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { KeyResultObjective } from '../shared/types/model/KeyResultObjective';
import { NotifierService } from '../shared/services/notifier.service';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamComponent {
  constructor(
    private dialog: MatDialog,
    private notifierService: NotifierService,
  ) {
    this.notifierService.objectivesChanges.subscribe((objectiveChange) => {
      if (objectiveChange.teamId != this.overviewEntity.value.team.id) {
        return;
      }

      let objectives = this.overviewEntity.value.objectives;
      const existingObjIndex = objectives.findIndex((obj) => obj.id === objectiveChange.objective.id);

      if (existingObjIndex == -1) {
        //Add
        objectives.push(objectiveChange.objective);
        return;
      }
      if (objectiveChange.delete) {
        //delete
        objectives = objectives.filter((e, i) => existingObjIndex != i);
      } else {
        //update
        objectives[existingObjIndex] = {
          ...objectives[existingObjIndex],
          title: objectiveChange.objective.title,
          state: objectiveChange.objective.state,
        };
      }
      this.overviewEntity = { ...this.overviewEntity.value, objectives: objectives };

      if (objectiveChange.addKeyResult) {
        //TODO Open Keyresult dialog
        console.log('Open new keyResult dialog');
      }
    });
  }

  private _overviewEntity = new BehaviorSubject<OverviewEntity>({} as unknown as OverviewEntity);

  @Input() get overviewEntity(): BehaviorSubject<OverviewEntity> {
    return this._overviewEntity;
  }

  set overviewEntity(overviewEntity: OverviewEntity) {
    this._overviewEntity.next(overviewEntity);
  }

  createObjective() {
    const matDialogRef = this.dialog.open(ObjectiveFormComponent, {
      data: { teamId: this.overviewEntity.value.team.id } as unknown as KeyResultObjective,
    });
    matDialogRef.afterClosed().subscribe((result) => {
      if (result.objective) {
        this.notifierService.objectivesChanges.next({
          objective: result.objective,
          teamId: result.teamId,
          delete: result.delete,
          addKeyResult: result.addKeyResult,
        });
      }
    });
  }
}
