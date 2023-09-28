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
  @Input()
  get overviewEntity(): BehaviorSubject<OverviewEntity> {
    return this._overviewEntity;
  }
  set overviewEntity(overviewEntity: OverviewEntity) {
    this._overviewEntity.next(overviewEntity);
  }
  private _overviewEntity = new BehaviorSubject<OverviewEntity>({} as unknown as OverviewEntity);

  constructor(
    private dialog: MatDialog,
    private notifierService: NotifierService,
  ) {
    this.notifierService.objectivesChanges.subscribe((objective) => {
      const objectives = this.overviewEntity.value.objectives;
      const existingObjIndex = objectives.findIndex((obj) => obj.id === objective.id);
      if (existingObjIndex !== -1) {
        objectives[existingObjIndex] = {
          ...objectives[existingObjIndex],
          title: objective.title,
          state: objective.state,
        };
      } else {
        objectives.push(objective);
      }
      this.overviewEntity = { ...this.overviewEntity.value, objectives: objectives };
    });
  }

  createObjective() {
    const matDialogRef = this.dialog.open(ObjectiveFormComponent, {
      data: { teamId: this.overviewEntity.value.team.id } as unknown as KeyResultObjective,
    });
    matDialogRef.afterClosed().subscribe((result) => {
      if (result.objective) {
        this.notifierService.objectivesChanges.next(result.objective);
      }
    });
  }
}
