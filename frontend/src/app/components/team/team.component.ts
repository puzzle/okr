import { ChangeDetectionStrategy, Component, Input, OnInit, TrackByFunction } from '@angular/core';
import { OverviewEntity } from '../../shared/types/model/OverviewEntity';
import { ObjectiveFormComponent } from '../../shared/dialog/objective-dialog/objective-form.component';
import { RefreshDataService } from '../../services/refresh-data.service';
import { Objective } from '../../shared/types/model/Objective';
import { KeyresultDialogComponent } from '../keyresult-dialog/keyresult-dialog.component';
import { ObjectiveMin } from '../../shared/types/model/ObjectiveMin';
import { DialogService } from '../../services/dialog.service';
import { ConfigService } from '../../services/config.service';
import { ClientConfig } from '../../shared/types/model/ClientConfig';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamComponent implements OnInit {
  @Input({ required: true })
  public overviewEntity!: OverviewEntity;
  addIconSrc: BehaviorSubject<string> = new BehaviorSubject('assets/icons/new-icon.svg');

  constructor(
    private dialogService: DialogService,
    private refreshDataService: RefreshDataService,
    private configService: ConfigService,
  ) {
    this.configService.config$.subscribe((config: ClientConfig) => {
      const configuredIconSrc = config.customStyles['okr-add-objective-icon'];
      if (configuredIconSrc) this.addIconSrc.next(configuredIconSrc);
    });
  }

  ngOnInit(): void {}

  trackByObjectiveId: TrackByFunction<ObjectiveMin> = (index, objective) => objective.id;

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
