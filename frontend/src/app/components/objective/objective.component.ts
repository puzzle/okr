import { Component, Input, ViewChild } from '@angular/core';
import { ObjectiveMin } from '../../shared/types/model/ObjectiveMin';
import { Router } from '@angular/router';
import { map, ReplaySubject, take } from 'rxjs';
import { RefreshDataService } from '../../services/refresh-data.service';
import { ObjectiveService } from '../../services/objective.service';
import { trackByFn } from '../../shared/common';
import { KeyresultDialogComponent } from '../keyresult-dialog/keyresult-dialog.component';
import { TranslateService } from '@ngx-translate/core';
import { DialogService } from '../../services/dialog.service';
import { ObjectiveMenuActionsService, ObjectiveMenuEntry } from '../../services/objective-menu-actions.service';
import { State } from '../../shared/types/enums/State';
import { MatMenuTrigger } from '@angular/material/menu';

@Component({
  selector: 'app-objective-column',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
})
export class ObjectiveComponent {
  @Input() isWritable!: boolean;
  public objective$ = new ReplaySubject<ObjectiveMin>();
  menuEntries = this.objective$.pipe(map((objective) => this.objectiveMenuActionsService.getMenu(objective)));
  protected readonly trackByFn = trackByFn;
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger | undefined;

  constructor(
    private readonly dialogService: DialogService,
    private readonly router: Router,
    private readonly refreshDataService: RefreshDataService,
    private readonly objectiveService: ObjectiveService,
    private readonly translate: TranslateService,
    private readonly objectiveMenuActionsService: ObjectiveMenuActionsService,
  ) {}

  @Input() set objective(objective: ObjectiveMin) {
    this.objective$.next(objective);
  }

  getStateTooltip(stateString: string): string {
    const state = this.getStateByValue(stateString);
    return this.translate.instant('INFORMATION.OBJECTIVE_STATE_TOOLTIP', { state: state });
  }

  redirect(menuEntry: ObjectiveMenuEntry, objectiveMin: ObjectiveMin) {
    const matDialogRef = menuEntry.action();
    matDialogRef
      .afterClosed()
      .pipe(take(1))
      .subscribe((result) => {
        this.objectiveService.getFullObjective(objectiveMin.id).subscribe((objective) => {
          menuEntry.afterAction(objective, result);
          this.trigger?.focus();
        });
      });
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

  isObjectiveComplete(objective: ObjectiveMin): boolean {
    return objective.state == State.SUCCESSFUL || objective.state == State.NOTSUCCESSFUL;
  }

  getStateByValue(value: string): string {
    return Object.keys(State).find((key) => State[key as keyof typeof State] === value) ?? '';
  }
}
