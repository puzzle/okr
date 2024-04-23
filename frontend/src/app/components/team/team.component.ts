import { ChangeDetectionStrategy, Component, Input, OnInit, TrackByFunction } from '@angular/core';
import { OverviewEntity } from '../../shared/types/model/OverviewEntity';
import { MatDialog } from '@angular/material/dialog';
import { ObjectiveFormComponent } from '../../shared/dialog/objective-dialog/objective-form.component';
import { RefreshDataService } from '../../services/refresh-data.service';
import { Objective } from '../../shared/types/model/Objective';
import { isMobileDevice } from '../../shared/common';
import { KeyresultDialogComponent } from '../keyresult-dialog/keyresult-dialog.component';
import { ObjectiveMin } from '../../shared/types/model/ObjectiveMin';

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
    private dialog: MatDialog,
    private refreshDataService: RefreshDataService,
  ) {}

  trackByObjectiveId: TrackByFunction<ObjectiveMin> = (index, objective) => objective.id;

  ngOnInit(): void {}

  createObjective() {
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

    const matDialogRef = this.dialog.open(ObjectiveFormComponent, {
      data: {
        objective: {
          teamId: this.overviewEntity.team.id,
        },
      },
      height: dialogConfig.height,
      width: dialogConfig.width,
      maxHeight: dialogConfig.maxHeight,
      maxWidth: dialogConfig.maxWidth,
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

    this.dialog
      .open(KeyresultDialogComponent, {
        height: dialogConfig.height,
        width: dialogConfig.width,
        maxHeight: dialogConfig.maxHeight,
        maxWidth: dialogConfig.maxWidth,
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
