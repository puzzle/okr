import { AfterViewInit, ChangeDetectionStrategy, Component, Input, OnInit, ViewChild } from '@angular/core';
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
import { KeyResultDialogComponent } from '../shared/dialog/key-result-dialog/key-result-dialog.component';
import { Objective } from '../shared/types/model/Objective';

@Component({
  selector: 'app-objective-column',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveComponent implements OnInit, AfterViewInit {
  menuEntries: MenuEntry[] = [];
  isComplete: boolean = false;

  constructor(
    private matDialog: MatDialog,
    private router: Router,
    private refreshDataService: RefreshDataService,
    private objectiveService: ObjectiveService,
  ) {}

  get objective(): BehaviorSubject<ObjectiveMin> {
    return this.objective$;
  }
  @Input()
  set objective(objective: ObjectiveMin) {
    this.objective$.next(objective);
  }
  private objective$ = new BehaviorSubject<ObjectiveMin>({} as ObjectiveMin);

  ngOnInit() {
    if (this.objective.value.state.includes('successful') || this.objective.value.state.includes('not-successful')) {
      this.isComplete = true;
    }
  }

  ngAfterViewInit(): void {
    if (this.objective.value.state.includes('successful') || this.objective.value.state.includes('not-successful')) {
      this.menuEntries = [
        { displayName: 'Objective wiedereröffnen', action: 'reopen' },
        { displayName: 'Objective duplizieren', action: 'duplicate' },
      ];
    } else {
      this.menuEntries = [
        {
          displayName: 'Objective bearbeiten',
          dialog: { dialog: ObjectiveFormComponent, data: { objectiveId: this.objective.value.id } },
        },
        { displayName: 'Objective duplizieren', action: 'duplicate' },
        {
          displayName: 'Objective abschliessen',
          action: 'complete',
          dialog: { dialog: CompleteDialogComponent, data: {} },
        },
        {
          displayName: 'Objective freigeben',
          action: 'release',
          dialog: {
            dialog: ConfirmDialogComponent,
            data: {
              title: 'Objective',
              action: 'release',
            },
          },
        },
      ];

      if (this.objective.value.state === State.ONGOING) {
        this.menuEntries = this.menuEntries.filter((entry) => entry.displayName !== 'Objective freigeben');
      }
      if (this.objective.value.state === State.DRAFT) {
        this.menuEntries = this.menuEntries.filter((entry) => entry.displayName !== 'Objective abschliessen');
      }
    }
  }

  redirect(menuEntry: MenuEntry) {
    if (menuEntry.dialog) {
      let dialogWidth = menuEntry.action == 'release' ? 'auto' : '45em';
      const matDialogRef = this.matDialog.open(menuEntry.dialog.dialog, {
        data: {
          title: menuEntry.dialog.data.title,
          action: menuEntry.action,
          objective: menuEntry.dialog.data,
        },
        width: dialogWidth,
      });
      matDialogRef.afterClosed().subscribe((result) => {
        if (result) {
          this.completeReleaseReload(menuEntry, result);
        }
      });
    } else {
      this.reopenRedirect(menuEntry);
    }
  }

  completeReleaseReload(menuEntry: MenuEntry, result: { endState: string; comment: string | null; objective: any }) {
    if (menuEntry.action) {
      this.objectiveService.getFullObjective(this.objective.value.id).subscribe((objective) => {
        if (menuEntry.action == 'complete') {
          this.completeObjective(objective, result);
        } else if (menuEntry.action == 'release') {
          this.releaseObjective(objective);
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
      objective: objective,
      comment: result.comment,
    };
    this.objectiveService.updateObjective(objective).subscribe(() => {
      this.objectiveService.createCompleted(completed).subscribe(() => {
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
      this.objectiveService.getFullObjective(this.objective.value.id).subscribe((objective) => {
        objective.state = 'ONGOING' as State;
        this.objectiveService.updateObjective(objective).subscribe(() => {
          this.objectiveService.deleteCompleted(objective.id).subscribe(() => {
            this.refreshDataService.markDataRefresh();
          });
        });
      });
    } else {
      this.router.navigate([menuEntry.route!]);
    }
  }

  openObjectiveDetail() {
    this.router.navigate(['objective', this.objective.value.id]);
  }

  openAddKeyResultDialog() {
    this.matDialog
      .open(KeyResultDialogComponent, {
        width: '45em',
        height: 'auto',
        data: {
          objective: this.objective.value,
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
