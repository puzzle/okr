import { ChangeDetectionStrategy, Component, Input, OnInit, TrackByFunction } from '@angular/core';
import { OverviewEntity } from '../../shared/types/model/OverviewEntity';
import { ObjectiveFormComponent } from '../../shared/dialog/objective-dialog/objective-form.component';
import { RefreshDataService } from '../../services/refresh-data.service';
import { Objective } from '../../shared/types/model/Objective';
import { isMobileDevice } from '../../shared/common';
import { KeyresultDialogComponent } from '../keyresult-dialog/keyresult-dialog.component';
import { ObjectiveMin } from '../../shared/types/model/ObjectiveMin';
import { DialogService } from '../../services/dialog.service';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamComponent implements OnInit {
  @Input({ required: true })
  public overviewEntity!: OverviewEntity;

  constructor(
    private dialogService: DialogService,
    private refreshDataService: RefreshDataService,
  ) {}

  trackByObjectiveId: TrackByFunction<ObjectiveMin> = (index, objective) => objective.id;

  ngOnInit(): void {}

  createObjective() {
    const matDialogRef = this.dialogService.open(ObjectiveFormComponent, {
      data: {
        objective: {
          teamId: this.overviewEntity.team.id,
        },
      },
    });
    matDialogRef.afterClosed().subscribe((result) => {
      if (result?.addKeyResult) {
        this.openAddKeyResultDialog(result.objective);
      }
      this.refreshDataService.markDataRefresh();
    });
  }

  openAddKeyResultDialog(objective: Objective) {
    const dialogConfig = isMobileDevice()
      ? {
          maxWidth: '100vw',
          maxHeight: '100vh',
          height: '100vh',
          width: '100vw',
        }
      : {
          width: '45em',
          height: 'auto',
        };

    this.dialogService
      .open(KeyresultDialogComponent, {
        data: {
          objective: objective,
          keyResult: null,
        },
      })
      .afterClosed()
      .subscribe((result) => {
        if (result?.openNew) {
          this.openAddKeyResultDialog(objective);
        }
        this.refreshDataService.markDataRefresh();
      });
  }
}
