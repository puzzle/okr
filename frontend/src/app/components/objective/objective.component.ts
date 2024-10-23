import { Component, Input, OnInit } from '@angular/core';
import { MenuEntry } from '../../shared/types/menu-entry';
import { ObjectiveMin } from '../../shared/types/model/ObjectiveMin';
import { Router } from '@angular/router';
import { BehaviorSubject, map } from 'rxjs';
import { RefreshDataService } from '../../services/refresh-data.service';
import { State } from '../../shared/types/enums/State';
import { ObjectiveService } from '../../services/objective.service';
import { Completed } from '../../shared/types/model/Completed';
import { Objective } from '../../shared/types/model/Objective';
import { isObjectiveComplete, trackByFn } from '../../shared/common';
import { KeyresultDialogComponent } from '../keyresult-dialog/keyresult-dialog.component';
import { TranslateService } from '@ngx-translate/core';
import { DialogService } from '../../services/dialog.service';
import { ObjectiveMenuActionsService, ObjectiveMenuEntry } from '../../services/objective-menu-actions.service';

@Component({
  selector: 'app-objective-column',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
})
export class ObjectiveComponent implements OnInit {
  @Input() isWritable!: boolean;
  isComplete: boolean = false;
  public objective$ = new BehaviorSubject<ObjectiveMin>({} as ObjectiveMin);
  menuEntries = this.objective$.pipe(map((objective) => this.objectiveMenuActionsService.getMenu(objective)));
  protected readonly trackByFn = trackByFn;
  protected readonly console = console;
  protected readonly isObjectiveComplete = isObjectiveComplete;

  constructor(
    private dialogService: DialogService,
    private router: Router,
    private refreshDataService: RefreshDataService,
    private objectiveService: ObjectiveService,
    private translate: TranslateService,
    private objectiveMenuActionsService: ObjectiveMenuActionsService,
  ) {}

  @Input() set objective(objective: ObjectiveMin) {
    this.objective$.next(objective);
  }

  ngOnInit() {}

  formatObjectiveState(state: string): string {
    const lastIndex = state.lastIndexOf('-');
    if (lastIndex !== -1) {
      return state.substring(0, lastIndex).toUpperCase();
    } else {
      return state.toUpperCase();
    }
  }

  getStateTooltip(): string {
    return this.translate.instant('INFORMATION.OBJECTIVE_STATE_TOOLTIP');
  }

  redirect(menuEntry: ObjectiveMenuEntry, objectiveMin: ObjectiveMin) {
    const matDialogRef = menuEntry.action();
    matDialogRef.afterClosed().subscribe((result) => {
      this.objectiveService.getFullObjective(objectiveMin.id).subscribe((objective) => {
        menuEntry.afterAction(objective, result);
      });
    });
  }

  handleDialogResult(menuEntry: MenuEntry, result: { endState: string; comment: string | null; objective: any }) {
    if (menuEntry.action) {
      this.objectiveService.getFullObjective(this.objective$.value.id).subscribe((objective) => {
        if (menuEntry.action == 'complete') {
        } else if (menuEntry.action == 'release') {
        } else if (menuEntry.action == 'duplicate') {
          this.refreshDataService.markDataRefresh();
        } else if (menuEntry.action == 'releaseBacklog') {
          this.refreshDataService.markDataRefresh();
        } else if (menuEntry.action == 'todraft') {
        }
      });
    } else {
      if (result?.objective) {
        this.refreshDataService.markDataRefresh();
      }
    }
  }

  openObjectiveDetail(objectiveId: number) {
    this.router.navigate(['details/objective', objectiveId]);
  }

  openAddKeyResultDialog(objective: ObjectiveMin) {
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
