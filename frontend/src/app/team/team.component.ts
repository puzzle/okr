import {ChangeDetectionStrategy, Component, Input, OnInit, TrackByFunction} from '@angular/core';
import {OverviewEntity} from '../shared/types/model/OverviewEntity';
import {MatDialog} from '@angular/material/dialog';
import {ObjectiveFormComponent} from '../shared/dialog/objective-dialog/objective-form.component';
import {RefreshDataService} from '../shared/services/refresh-data.service';
import {Objective} from '../shared/types/model/Objective';
import {isMobileDevice, optionalReplaceWithNulls} from '../shared/common';
// import { TeamManagementComponent } from '../shared/dialog/team-management/team-management.component';
import {TeamMin} from '../shared/types/model/TeamMin';
import {KeyresultDialogComponent} from '../shared/dialog/keyresult-dialog/keyresult-dialog.component';
import {ActivatedRoute, Router} from '@angular/router';
import {ObjectiveMin} from '../shared/types/model/ObjectiveMin';
import {UserService} from '../shared/services/user.service';
import {extractAdminTeamsFromUser} from '../shared/types/model/User';
import {AddEditTeamDialog} from "../shared/dialog/team-management/add-edit-team-dialog.component";
import {CloseState} from "../shared/types/enums/CloseState";

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamComponent implements OnInit {
  @Input({ required: true })
  public overviewEntity!: OverviewEntity;

  public currentUserHasAdminRightsForTeam: boolean = false;

  trackByObjectiveId: TrackByFunction<ObjectiveMin> = (index, objective) => objective.id;

  constructor(
    private dialog: MatDialog,
    private refreshDataService: RefreshDataService,
    private router: Router,
    private route: ActivatedRoute,
    private userService: UserService,
  ) {}

  ngOnInit(): void {
    this.setCurrentUserIsAdminForTeam();
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
          teamId: this.overviewEntity.team.id,
          teamVersion: this.overviewEntity.team.version,
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
    const dialog = this.dialog.open(AddEditTeamDialog, {
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

  public setCurrentUserIsAdminForTeam(): void {
    const user = this.userService.getCurrentUser();
    if (user.isOkrChampion) {
      this.currentUserHasAdminRightsForTeam = true;
      return;
    }
    const teams = extractAdminTeamsFromUser(user);
    const currentTeamId = this.overviewEntity.team.id;
    this.currentUserHasAdminRightsForTeam = teams.some((t) => t.id === currentTeamId);
  }
}
