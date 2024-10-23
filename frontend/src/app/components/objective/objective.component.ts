import { ChangeDetectionStrategy, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { MenuEntry } from '../../shared/types/menu-entry';
import { ObjectiveMin } from '../../shared/types/model/ObjectiveMin';
import { Router } from '@angular/router';
import { ObjectiveFormComponent } from '../../shared/dialog/objective-dialog/objective-form.component';
import { BehaviorSubject } from 'rxjs';
import { RefreshDataService } from '../../services/refresh-data.service';
import { State } from '../../shared/types/enums/State';
import { ObjectiveService } from '../../services/objective.service';
import { ConfirmDialogComponent } from '../../shared/dialog/confirm-dialog/confirm-dialog.component';
import { CompleteDialogComponent } from '../../shared/dialog/complete-dialog/complete-dialog.component';
import { Completed } from '../../shared/types/model/Completed';
import { Objective } from '../../shared/types/model/Objective';
import { trackByFn } from '../../shared/common';
import { KeyresultDialogComponent } from '../keyresult-dialog/keyresult-dialog.component';
import { TranslateService } from '@ngx-translate/core';
import { GJ_REGEX_PATTERN } from '../../shared/constantLibary';
import { DialogService } from '../../services/dialog.service';
import { ObjectiveMenuActionsService, ObjectiveMenuEntry } from '../../services/objective-menu-actions.service';

@Component({
  selector: 'app-objective-column',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveComponent implements OnInit {
  @Input()
  isWritable!: boolean;
  isComplete: boolean = false;
  protected readonly trackByFn = trackByFn;
  @ViewChild('menuButton') private menuButton!: ElementRef;

  constructor(
    private dialogService: DialogService,
    private router: Router,
    private refreshDataService: RefreshDataService,
    private objectiveService: ObjectiveService,
    private translate: TranslateService,
    private objectiveMenuActionsService: ObjectiveMenuActionsService,
  ) {}

  @Input()
  set objective(objective: ObjectiveMin) {
    this.objective$.next(objective);
  }

  public objective$ = new BehaviorSubject<ObjectiveMin>({} as ObjectiveMin);

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

  isObjectiveComplete(objective: ObjectiveMin): boolean {
    return objective.state == State.SUCCESSFUL || objective.state == State.NOTSUCCESSFUL;
  }

  getMenu(objective: ObjectiveMin): ObjectiveMenuEntry[] {
    if (this.isObjectiveComplete(objective)) {
      return this.objectiveMenuActionsService.getCompletedMenuActions(objective);
    } else if (objective.state === State.ONGOING) {
      return this.objectiveMenuActionsService.getOngoingMenuActions(objective);
    } else if (objective.state === State.DRAFT) {
      return this.objectiveMenuActionsService.getDraftMenuActions(objective);
    }
    //Probably throw an error here
    return [];
  }

  redirect(menuEntry: ObjectiveMenuEntry) {
    // if (menuEntry.dialog) {
    //   const matDialogRef = this.dialogService.open(menuEntry.dialog.dialog, {
    //     data: {
    //       title: menuEntry.dialog.data.title,
    //       action: menuEntry.action,
    //       text: menuEntry.action,
    //       objective: menuEntry.dialog.data,
    //       objectiveTitle: menuEntry.dialog.data.objectiveTitle,
    //     },
    //     ...((menuEntry.action == 'release' || menuEntry.action == 'todraft') && { width: 'auto' }),
    //   });
    //   matDialogRef.afterClosed().subscribe((result) => {
    //     this.menuButton.nativeElement.focus();
    //     if (result) {
    //       this.handleDialogResult(menuEntry, result);
    //     }
    //   });
    // } else {
    //   this.reopenRedirect(menuEntry);
    // }
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
    } else {
      this.router.navigate([menuEntry.route!]);
    }
  }

  openObjectiveDetail(objectiveId: number) {
    this.router.navigate(['details/objective', objectiveId]);
  }

  openAddKeyResultDialog() {
    this.dialogService
      .open(KeyresultDialogComponent, {
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
