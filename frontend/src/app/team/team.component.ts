import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { MatDialog } from '@angular/material/dialog';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { BehaviorSubject } from 'rxjs';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { Objective } from '../shared/types/model/Objective';
import { KeyResultDialogComponent } from '../shared/dialog/key-result-dialog/key-result-dialog.component';
import { trackByFn } from '../shared/common';
import { TeamManagementComponent } from '../shared/dialog/team-management/team-management.component';
import { TeamMin } from '../shared/types/model/TeamMin';
import { OrganisationService } from '../shared/services/organisation.service';
import { OrganisationState } from '../shared/types/enums/OrganisationState';

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
  hasWriteAllAccess!: boolean;
  hasInActiveOrganisation = new BehaviorSubject<boolean>(false);

  constructor(
    private dialog: MatDialog,
    private refreshDataService: RefreshDataService,
    private organisationService: OrganisationService,
  ) {
    this.checkIfTeamHasInActiveOrganisations();
  }

  checkIfTeamHasInActiveOrganisations() {
    if (this.hasWriteAllAccess) {
      this.overviewEntity$.subscribe((result) => {
        if (result.team) {
          this.organisationService.getOrganisationsByTeamId(result.team.id).subscribe((organisations) => {
            this.hasInActiveOrganisation.next(
              organisations.filter((organisation) => organisation.state != OrganisationState.ACTIVE).length > 0,
            );
          });
        }
      });
    }
  }

  @Input()
  get overviewEntity(): BehaviorSubject<OverviewEntity> {
    return this.overviewEntity$;
  }

  set overviewEntity(overviewEntity: OverviewEntity) {
    this.overviewEntity$.next(overviewEntity);
  }

  createObjective() {
    const matDialogRef = this.dialog.open(ObjectiveFormComponent, {
      data: { objective: { teamId: this.overviewEntity.value.team.id } },
      width: '45em',
    });
    matDialogRef.afterClosed().subscribe((result) => {
      if (result?.addKeyResult) {
        this.openAddKeyResultDialog(result.objective);
      }
      this.refreshDataService.markDataRefresh();
    });
  }

  openAddKeyResultDialog(objective: Objective) {
    this.dialog
      .open(KeyResultDialogComponent, {
        width: '45em',
        height: 'auto',
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
