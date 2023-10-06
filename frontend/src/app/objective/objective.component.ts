import { AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, Input } from '@angular/core';
import { MenuEntry } from '../shared/types/menu-entry';
import { RouteService } from '../shared/services/route.service';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { Router } from '@angular/router';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { MatDialog } from '@angular/material/dialog';
import { NotifierService } from '../shared/services/notifier.service';
import { KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';

@Component({
  selector: 'app-objective-column',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveComponent implements AfterViewInit {
  @Input() objective!: ObjectiveMin;
  menuEntries: MenuEntry[] = [];

  constructor(
    private dialog: MatDialog,
    private routeService: RouteService,
    private notifierService: NotifierService,
    private matDialog: MatDialog,
    private router: Router,
  ) {
    this.notifierService.keyResultsChanges.subscribe((keyResultChange) => {
      const keyResults = this.objective.keyResults;
      if (!keyResults) {
        return;
      }
      if (keyResultChange.delete) {
        const existingKRIndex = keyResults.findIndex((kr) => kr.id === keyResultChange.keyResult.id);
        keyResults.splice(existingKRIndex, 1);
        this.objective = { ...this.objective, keyResults: keyResults };
      } else {
        if (keyResultChange.objective.id != this.objective.id) {
          return;
        }
        const existingKRIndex = keyResults.findIndex((kr) => kr.id === keyResultChange.changeId);
        if (existingKRIndex !== -1) {
          keyResults[existingKRIndex] = {
            ...keyResults[existingKRIndex],
            id: keyResultChange.keyResult.id,
            title: keyResultChange.keyResult.title,
          };
        } else {
          keyResults.push(keyResultChange.keyResult);
        }
        this.objective = { ...this.objective, keyResults: keyResults };
      }
    });

    this.notifierService.openKeyresultCreation.subscribe((objective) => {
      if (objective.id === this.objective.id) {
        this.objective = objective;
        this.openAddKeyResultDialog();
      }
    });
  }

  ngAfterViewInit(): void {
    this.menuEntries = [
      {
        displayName: 'Objective bearbeiten',
        dialog: { dialog: ObjectiveFormComponent, data: { objectiveId: this.objective.id } },
      },
      { displayName: 'Objective duplizieren' },
      { displayName: 'Objective abschliessen' },
      { displayName: 'Objective freigeben' },
    ];
  }

  redirect(menuEntry: MenuEntry) {
    if (menuEntry.dialog) {
      const matDialogRef = this.matDialog.open(menuEntry.dialog.dialog, { data: menuEntry.dialog.data });
      matDialogRef.afterClosed().subscribe((result) => {
        if (result?.objective) {
          this.notifierService.objectivesChanges.next({
            objective: result.objective,
            teamId: result.teamId,
            delete: result.delete,
            addKeyResult: result.addKeyResult,
          });
        }
      });
    } else {
      this.routeService.navigate(menuEntry.route!);
    }
  }

  openDialog() {
    throw new Error(
      'This function should not have been called, since openDialog should be false, even though it appears to be true!',
    );
  }

  openObjectiveDetail() {
    this.router.navigate(['objective', this.objective.id]);
  }

  openAddKeyResultDialog() {
    this.dialog
      .open(KeyResultDialogComponent, {
        width: '45em',
        height: 'auto',
        data: {
          objective: this.objective,
          keyResult: null,
        },
      })
      .afterClosed()
      .subscribe(async (result) => {
        if (result == undefined || result.keyResult == null) {
          return;
        }
        await this.notifierService.keyResultsChanges.next({
          keyResult: result.keyResult,
          changeId: null,
          objective: result.objective,
          delete: false,
        });
        if (result.openNew) {
          this.openAddKeyResultDialog();
        }
      });
  }
}
