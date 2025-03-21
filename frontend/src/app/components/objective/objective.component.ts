import { Component, Input, ViewChild } from '@angular/core';
import { ObjectiveMin } from '../../shared/types/model/objective-min';
import { Router } from '@angular/router';
import { distinct, map, ReplaySubject, take } from 'rxjs';
import { RefreshDataService } from '../../services/refresh-data.service';
import { getSvgForState, trackByFn } from '../../shared/common';
import { KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';
import { TranslateService } from '@ngx-translate/core';
import { DialogService } from '../../services/dialog.service';
import { ObjectiveMenuActionsService, ObjectiveMenuEntry } from '../../services/objective-menu-actions.service';
import { State } from '../../shared/types/enums/state';
import { MatMenuTrigger } from '@angular/material/menu';

@Component({
  selector: 'app-objective',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
  standalone: false
})
export class ObjectiveComponent {
  @Input() isWritable!: boolean;

  public objective$ = new ReplaySubject<ObjectiveMin>();

  menuEntries = this.objective$
    .pipe(distinct())
    .pipe(map((objective) => this.objectiveMenuActionsService.getMenu(objective)));

  protected readonly trackByFn = trackByFn;

  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger | undefined;

  constructor(
    private readonly dialogService: DialogService,
    private readonly router: Router,
    private readonly refreshDataService: RefreshDataService,
    private readonly translate: TranslateService,
    private readonly objectiveMenuActionsService: ObjectiveMenuActionsService
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
        if (result) {
          menuEntry.afterAction(objectiveMin, result);
        }
        this.trigger?.focus();
      });
  }

  openObjectiveDetail(objectiveId: number) {
    this.router.navigate(['details/objective',
      objectiveId]);
  }

  openAddKeyResultDialog(objective: ObjectiveMin) {
    this.dialogService
      .open(KeyResultDialogComponent, {
        data: {
          objective: objective,
          keyResult: null
        }
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
    return Object.keys(State)
      .find((key) => State[key as keyof typeof State] === value) ?? '';
  }

  protected readonly getSvgForState = getSvgForState;
}
