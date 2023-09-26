import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { MatDialog } from '@angular/material/dialog';
import { ObjectiveComponent } from '../objective/objective.component';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { KeyResultObjective } from '../shared/types/model/KeyResultObjective';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamComponent {
  @Input() overviewEntity!: OverviewEntity;

  constructor(private dialog: MatDialog) {}

  createObjective() {
    this.dialog.open(ObjectiveFormComponent, {
      data: { teamId: this.overviewEntity.team.id } as unknown as KeyResultObjective,
    });
  }
}
