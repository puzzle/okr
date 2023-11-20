import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { MatDialog } from '@angular/material/dialog';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { BehaviorSubject, ReplaySubject } from 'rxjs';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { Objective } from '../shared/types/model/Objective';
import { isMobileDevice, trackByFn } from '../shared/common';
import { TeamManagementComponent } from '../shared/dialog/team-management/team-management.component';
import { TeamMin } from '../shared/types/model/TeamMin';
import { KeyresultDialogComponent } from '../shared/dialog/keyresult-dialog/keyresult-dialog.component';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamComponent {
  private overviewEntity$ = new BehaviorSubject<OverviewEntity>({} as OverviewEntity);
  protected readonly trackByFn = trackByFn;

  @Input()
  hasAdminAccess!: ReplaySubject<boolean>;

  constructor(
    private dialog: MatDialog,
    private refreshDataService: RefreshDataService,
  ) {}

  @Input()
  get overviewEntity(): BehaviorSubject<OverviewEntity> {
    return this.overviewEntity$;
  }

  set overviewEntity(overviewEntity: OverviewEntity) {
    this.overviewEntity$.next(overviewEntity);
  }

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
          teamId: this.overviewEntity.value.team.id,
          teamVersion: this.overviewEntity.value.team.version,
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

  openEditTeamDialog(team: TeamMin) {
    const dialog = this.dialog.open(TeamManagementComponent, {
      width: '45em',
      height: 'auto',
      data: {
        team: team,
      },
    });
    dialog.afterClosed().subscribe(() => {
      this.refreshDataService.markDataRefresh();
    });
  }
}
