import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { ObjectiveMin } from '../../shared/types/model/ObjectiveMin';
import { Router } from '@angular/router';
import { ObjectiveFormComponent } from '../../shared/dialog/objective-dialog/objective-form.component';
import { BehaviorSubject } from 'rxjs';
import { RefreshDataService } from '../../services/refresh-data.service';
import { ObjectiveService } from '../../services/objective.service';
import { trackByFn } from '../../shared/common';
import { KeyresultDialogComponent } from '../keyresult-dialog/keyresult-dialog.component';
import { TranslateService } from '@ngx-translate/core';
import { GJ_REGEX_PATTERN } from '../../shared/constantLibary';
import { DialogService } from '../../services/dialog.service';

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
    private dialogService: DialogService,
    private router: Router,
    private refreshDataService: RefreshDataService,
    private objectiveService: ObjectiveService,
    private translate: TranslateService,
  ) {}

  @Input() set objective(objective: ObjectiveMin) {
    this.objective$.next(objective);
  }

  getStateTooltip(stateString: string): string {
    const state = this.getStateByValue(stateString);
    return this.translate.instant('INFORMATION.OBJECTIVE_STATE_TOOLTIP', { state: state });
  }

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

  getMenu(): void {
    if (this.objective$.value.state.includes('successful') || this.objective$.value.state.includes('not-successful')) {
      this.menuEntries = this.getCompletedMenuActions();
    } else {
      if (this.objective$.value.state === State.ONGOING) {
        this.menuEntries = this.getOngoingMenuActions();
      } else {
        this.menuEntries = this.getDraftMenuActions();
      }
    }
  }

  getOngoingMenuActions() {
    return [
      ...this.getDefaultMenuActions(),
      ...[
        {
          displayName: 'Objective abschliessen',
          action: 'complete',
          dialog: { dialog: CompleteDialogComponent, data: { objectiveTitle: this.objective$.value.title } },
        },
        {
          displayName: 'Objective als Draft speichern',
          action: 'todraft',
          dialog: {
            dialog: ConfirmDialogComponent,
            data: {
              title: this.translate.instant('CONFIRMATION.TO_DRAFT.TITLE'),
              text: this.translate.instant('CONFIRMATION.TO_DRAFT.TEXT'),
            },
          },
        },
      ],
    ];
  }

  getDraftMenuActions() {
    const action = this.isBacklogQuarter ? 'releaseBacklog' : 'release';
    let menuEntries = {
      displayName: 'Objective veröffentlichen',
      action: action,
      dialog: {
        dialog: this.isBacklogQuarter ? ObjectiveFormComponent : ConfirmDialogComponent,
        data: {
          title: this.translate.instant('CONFIRMATION.RELEASE.TITLE'),
          text: this.translate.instant('CONFIRMATION.RELEASE.TEXT'),
          action: action,
          objectiveId: this.isBacklogQuarter ? this.objective$.value.id : undefined,
        },
      },
    };

    return [...this.getDefaultMenuActions(), menuEntries];
  }

  getDefaultMenuActions() {
    return [
      {
        displayName: 'Objective bearbeiten',
        dialog: { dialog: ObjectiveFormComponent, data: { objectiveId: this.objective$.value.id } },
      },
      {
        displayName: 'Objective duplizieren',
        action: 'duplicate',
        dialog: { dialog: ObjectiveFormComponent, data: { objectiveId: this.objective$.value.id } },
      },
    ];
  }

  getCompletedMenuActions() {
    return [
      { displayName: 'Objective wiedereröffnen', action: 'reopen' },
      {
        displayName: 'Objective duplizieren',
        action: 'duplicate',
        dialog: { dialog: ObjectiveFormComponent, data: { objectiveId: this.objective$.value.id } },
      },
    ];
  }

  redirect(menuEntry: MenuEntry) {
    if (menuEntry.dialog) {
      const matDialogRef = this.dialogService.open(menuEntry.dialog.dialog, {
        data: {
          title: menuEntry.dialog.data.title,
          action: menuEntry.action,
          text: menuEntry.dialog.data.text,
          objective: menuEntry.dialog.data,
          objectiveTitle: menuEntry.dialog.data.objectiveTitle,
        },
        ...((menuEntry.action == 'release' || menuEntry.action == 'todraft') && { width: 'auto' }),
      });
      matDialogRef.afterClosed().subscribe((result) => {
        this.menuButton.nativeElement.focus();
        if (result) {
          this.handleDialogResult(menuEntry, result);
        }
      });
    } else {
      this.reopenRedirect(menuEntry);
    }
  }

  handleDialogResult(menuEntry: MenuEntry, result: { endState: string; comment: string | null; objective: any }) {
    if (menuEntry.action) {
      this.objectiveService.getFullObjective(this.objective$.value.id).subscribe((objective) => {
        if (menuEntry.action == 'complete') {
          this.completeObjective(objective, result);
        } else if (menuEntry.action == 'release') {
          this.releaseObjective(objective);
        } else if (menuEntry.action == 'duplicate') {
          this.refreshDataService.markDataRefresh();
        } else if (menuEntry.action == 'releaseBacklog') {
          this.refreshDataService.markDataRefresh();
        } else if (menuEntry.action == 'todraft') {
          this.objectiveBackToDraft(objective);
        }
      });
    } else {
      if (result?.objective) {
        this.refreshDataService.markDataRefresh();
      }
    }
  }

  completeObjective(objective: Objective, result: { endState: string; comment: string | null; objective: any }) {
    objective.state = result.endState as State;
    const completed: Completed = {
      id: null,
      version: objective.version,
      objective: objective,
      comment: result.comment,
    };
    this.objectiveService.updateObjective(objective).subscribe(() => {
      this.objectiveService.createCompleted(completed).subscribe(() => {
        this.isComplete = true;
        this.refreshDataService.markDataRefresh();
      });
    });
  }

  releaseObjective(objective: Objective) {
    objective.state = 'ONGOING' as State;
    this.objectiveService.updateObjective(objective).subscribe(() => {
      this.refreshDataService.markDataRefresh();
    });
  }

  objectiveBackToDraft(objective: Objective) {
    objective.state = 'DRAFT' as State;
    this.objectiveService.updateObjective(objective).subscribe(() => {
      this.refreshDataService.markDataRefresh();
    });
  }

  reopenRedirect(menuEntry: MenuEntry) {
    if (menuEntry.action === 'reopen') {
      this.objectiveService.getFullObjective(this.objective$.value.id).subscribe((objective) => {
        objective.state = 'ONGOING' as State;
        this.objectiveService.updateObjective(objective).subscribe(() => {
          this.objectiveService.deleteCompleted(objective.id).subscribe(() => {
            this.isComplete = false;
            this.refreshDataService.markDataRefresh();
          });
        });
      });
  }

  openObjectiveDetail(objectiveId: number) {
    this.router.navigate(['details/objective', objectiveId]);
  }

  openAddKeyResultDialog() {
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
