import {ChangeDetectionStrategy, Component, ElementRef, Input, ViewChild} from '@angular/core';
import {OverviewEntity} from '../shared/types/model/OverviewEntity';
import {MatDialog} from '@angular/material/dialog';
import {ObjectiveFormComponent} from '../shared/dialog/objective-dialog/objective-form.component';
import {BehaviorSubject} from 'rxjs';
import {RefreshDataService} from '../shared/services/refresh-data.service';
import {KeyResultDialogComponent} from '../key-result-dialog/key-result-dialog.component';
import {Objective} from '../shared/types/model/Objective';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamComponent {
  @ViewChild('objective-column')
  objectiveColumn!: ElementRef<HTMLElement | undefined>;

  private overviewEntity$ = new BehaviorSubject<OverviewEntity>({} as OverviewEntity);

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
    const matDialogRef = this.dialog.open(ObjectiveFormComponent, {
      data: { teamId: this.overviewEntity.value.team.id },
      width: '850px',
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
}
