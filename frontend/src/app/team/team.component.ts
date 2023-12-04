import { ChangeDetectionStrategy, Component, Input, TrackByFunction } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { MatDialog } from '@angular/material/dialog';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { BehaviorSubject, ReplaySubject } from 'rxjs';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { Objective } from '../shared/types/model/Objective';
import { isMobileDevice, optionalReplaceWithNulls } from '../shared/common';
import { TeamManagementComponent } from '../shared/dialog/team-management/team-management.component';
import { TeamMin } from '../shared/types/model/TeamMin';
import { KeyresultDialogComponent } from '../shared/dialog/keyresult-dialog/keyresult-dialog.component';
import { ActivatedRoute, Router } from '@angular/router';
import { CloseState } from '../shared/types/enums/CloseState';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamComponent {
  private overviewEntity$ = new BehaviorSubject<OverviewEntity>({} as OverviewEntity);
  trackByObjectiveId: TrackByFunction<ObjectiveMin> = (index, objective) => objective.id;

  @Input()
  hasAdminAccess!: ReplaySubject<boolean>;

  constructor(
    private dialog: MatDialog,
    private refreshDataService: RefreshDataService,
    private router: Router,
    private route: ActivatedRoute,
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
    const dialog = this.dialog.open(TeamManagementComponent, {
      height: dialogConfig.height,
      width: dialogConfig.width,
      maxHeight: dialogConfig.maxHeight,
      maxWidth: dialogConfig.maxWidth,
      data: {
        team: team,
      },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result) {
        if (result.state == CloseState.DELETED) {
          this.removeTeam(result.id).then(() => this.refreshDataService.markDataRefresh());
        } else {
          this.refreshDataService.markDataRefresh();
        }
      }
    });
  }

  removeTeam(id: string) {
    let currentTeams = this.route.snapshot.queryParams['teams'].split(',');
    currentTeams = currentTeams.filter((cid: any) => cid != id);
    const params = { teams: currentTeams.join(',') };
    const optionalParams = optionalReplaceWithNulls(params);
    return this.router.navigate([], { queryParams: optionalParams });
  }
}
