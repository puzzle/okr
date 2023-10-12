import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { MatDialog } from '@angular/material/dialog';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { BehaviorSubject } from 'rxjs';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamComponent {
  private _overviewEntity = new BehaviorSubject<OverviewEntity>({} as OverviewEntity);

  constructor(
    private dialog: MatDialog,
    private refreshDataService: RefreshDataService,
  ) {}

  @Input()
  get overviewEntity(): BehaviorSubject<OverviewEntity> {
    return this._overviewEntity;
  }

  set overviewEntity(overviewEntity: OverviewEntity) {
    this._overviewEntity.next(overviewEntity);
  }

  createObjective() {
    const matDialogRef = this.dialog.open(ObjectiveFormComponent, {
      data: { teamId: this.overviewEntity.value.team.id },
      width: '850px',
    });
    matDialogRef.afterClosed().subscribe(() => {
      this.refreshDataService.markDataRefresh();
    });
  }
}
