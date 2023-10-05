import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { MatDialog } from '@angular/material/dialog';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { NotifierService } from '../shared/services/notifier.service';
import { BehaviorSubject } from 'rxjs';
import { ObjectiveService } from '../shared/services/objective.service';
import { keyResult } from '../shared/testData';

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
    private changeDetectionRef: ChangeDetectorRef,
  ) {
    this.notifierService.objectivesChanges.subscribe((objectiveChange) => {
      if (objectiveChange.teamId !== this.overviewEntity.value.team.id) {
        return;
      }

      const objectives = [...this.overviewEntity.value.objectives];
      const existingObjIndex = objectives.findIndex((obj) => obj.id === objectiveChange.objective.id);

      if (existingObjIndex === -1 && !objectiveChange.delete) {
        // Add
        objectives.push(objectiveChange.objective);
      } else if (existingObjIndex !== -1 && objectiveChange.delete) {
        // Delete
        objectives.splice(existingObjIndex, 1);
      } else if (existingObjIndex !== -1) {
        // Update
        objectives[existingObjIndex] = {
          ...objectives[existingObjIndex],
          title: objectiveChange.objective.title,
          state: objectiveChange.objective.state,
        };
      }

      this.overviewEntity = { ...this.overviewEntity.value, objectives };

      if (objectiveChange.addKeyResult) {
        this.changeDetectionRef.detectChanges();
        this.notifierService.openKeyresultCreation.next(objectiveChange.objective);
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
      data: { teamId: this.overviewEntity.value.team.id },
    });
    matDialogRef.afterClosed().subscribe((result) => {
      if (result?.objective) {
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
