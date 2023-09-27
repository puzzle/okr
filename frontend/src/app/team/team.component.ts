import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { MatDialog } from '@angular/material/dialog';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { KeyResultObjective } from '../shared/types/model/KeyResultObjective';
import { NotifierService } from '../shared/services/notifier.service';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamComponent {
  @Input() overviewEntity!: OverviewEntity;

  constructor(
    private dialog: MatDialog,
    private notifierService: NotifierService,
  ) {
    this.notifierService.objectivesChanges.subscribe((objective) => {
      console.log(objective);
      const existingObjIndex = this.overviewEntity.objectives.findIndex((obj) => obj.id === objective.id);
      if (existingObjIndex !== -1) {
        this.overviewEntity.objectives[existingObjIndex] = {
          ...this.overviewEntity.objectives[existingObjIndex],
          title: objective.title,
          state: objective.state,
        };
      } else {
        this.overviewEntity.objectives.push(objective);
      }
    });
  }

  createObjective() {
    const matDialogRef = this.dialog.open(ObjectiveFormComponent, {
      data: { teamId: this.overviewEntity.team.id } as unknown as KeyResultObjective,
    });
    matDialogRef.afterClosed().subscribe((result) => {
      if (result.objective) {
        this.notifierService.objectivesChanges.next(result.objective);
      }
    });
  }
}
