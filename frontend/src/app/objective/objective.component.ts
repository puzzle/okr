import { ChangeDetectionStrategy, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { MenuEntry } from '../shared/types/menu-entry';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { Router } from '@angular/router';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { MatDialog } from '@angular/material/dialog';
import { BehaviorSubject } from 'rxjs';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { State } from '../shared/types/enums/State';
import { ObjectiveService } from '../shared/services/objective.service';
import { ConfirmDialogComponent } from '../shared/dialog/confirm-dialog/confirm-dialog.component';
import { CompleteDialogComponent } from '../shared/dialog/complete-dialog/complete-dialog.component';
import { Completed } from '../shared/types/model/Completed';
import { Objective } from '../shared/types/model/Objective';
import { isMobileDevice, trackByFn } from '../shared/common';
import { KeyresultDialogComponent } from '../shared/dialog/keyresult-dialog/keyresult-dialog.component';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-objective-column',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveComponent implements OnInit {
  @Input()
  isWritable!: boolean;

  menuEntries: MenuEntry[] = [];
  isComplete: boolean = false;
  protected readonly trackByFn = trackByFn;
  @ViewChild('menuButton') private menuButton!: ElementRef;

  constructor(
    private matDialog: MatDialog,
    private router: Router,
    private refreshDataService: RefreshDataService,
    private objectiveService: ObjectiveService,
    private translate: TranslateService,
  ) {}

  @Input()
  set objective(objective: ObjectiveMin) {
    this.objective$.next(objective);
  }

  public objective$ = new BehaviorSubject<ObjectiveMin>({} as ObjectiveMin);

  ngOnInit() {
    if (this.objective$.value.state.includes('successful') || this.objective$.value.state.includes('not-successful')) {
      this.isComplete = true;
    }
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
      ],
    ];
  }

  getDraftMenuActions() {
    return [
      ...this.getDefaultMenuActions(),
      ...[
        {
          displayName: 'Objective veröffentlichen',
          action: 'release',
          dialog: {
            dialog: ConfirmDialogComponent,
            data: { title: 'Objective', action: 'release' },
          },
        },
      ],
    ];
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

      if (menuEntry.action == 'release') {
        dialogConfig.width = 'auto';
      }
      const matDialogRef = this.matDialog.open(menuEntry.dialog.dialog, {
        data: {
          title: menuEntry.dialog.data.title,
          action: menuEntry.action,
          objective: menuEntry.dialog.data,
          objectiveTitle: menuEntry.dialog.data.objectiveTitle,
        },
        height: dialogConfig.height,
        width: dialogConfig.width,
        maxHeight: dialogConfig.maxHeight,
        maxWidth: dialogConfig.maxWidth,
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
    } else {
      this.router.navigate([menuEntry.route!]);
    }
  }

  openObjectiveDetail() {
    this.router.navigate(['objective', this.objective$.value.id]);
  }

  openAddKeyResultDialog() {
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

    this.matDialog
      .open(KeyresultDialogComponent, {
        height: dialogConfig.height,
        width: dialogConfig.width,
        maxHeight: dialogConfig.maxHeight,
        maxWidth: dialogConfig.maxWidth,
        data: {
          objective: this.objective$.value,
          keyResult: null,
        },
      })
      .afterClosed()
      .subscribe((result) => {
        if (result?.openNew) {
          this.openAddKeyResultDialog();
        }
        this.refreshDataService.markDataRefresh();
      });
  }
}
