import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { MenuEntry } from '../shared/types/menu-entry';
import { RouteService } from '../shared/services/route.service';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { NotifierService } from '../shared/services/notifier.service';
import { Router } from '@angular/router';
import { KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-objective-column',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveComponent {
  @Input()
  get objective(): BehaviorSubject<ObjectiveMin> {
    return this._objective;
  }
  set objective(objective: ObjectiveMin) {
    this._objective.next(objective);
  }
  private _objective = new BehaviorSubject<ObjectiveMin>({} as unknown as ObjectiveMin);

  @Input() objectiveMin!: ObjectiveMin;
  menuEntries: MenuEntry[] = [
    { displayName: 'Objective bearbeiten', showDialog: false },
    { displayName: 'Objective duplizieren', showDialog: false },
    { displayName: 'Objective abschliessen', showDialog: false },
    { displayName: 'Objective freigeben', showDialog: false },
  ];

  constructor(
    private dialog: MatDialog,
    private routeService: RouteService,
    private notifierService: NotifierService,
    private router: Router,
  ) {
    this.notifierService.keyResultsChanges.subscribe((keyResultChange) => {
      if (keyResultChange.objective.id != this.objective.value.id) {
        return;
      }
      const keyResults = this.objective.value.keyResults;
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
      this.objective = { ...this.objective.value, keyResults: keyResults };
    });

    this.notifierService.deleteKeyResult.subscribe((keyResultToDelete) => {
      const keyResults = this.objective.value.keyResults;
      const existingKRIndex = keyResults.findIndex((kr) => kr.id === keyResultToDelete.id);
      keyResults.splice(existingKRIndex, 1);
      this.objective = { ...this.objective.value, keyResults: keyResults };
    });
  }

  redirect(menuEntry: MenuEntry) {
    if (menuEntry.showDialog) {
      this.openDialog();
    } else {
      this.routeService.navigate(menuEntry.routeLine!);
    }
  }

  openDialog() {
    throw new Error(
      'This function should not have been called, since openDialog should be false, even though it appears to be true!',
    );
  }

  openObjectiveDetail() {
    this.router.navigate(['objective', this.objective.value.id]);
  }

  openAddKeyResultDialog() {
    this.dialog
      .open(KeyResultDialogComponent, {
        width: '45em',
        height: 'auto',
        data: {
          objective: this.objective.value,
          keyResult: null,
        },
      })
      .afterClosed()
      .subscribe(async (result) => {
        await this.notifierService.keyResultsChanges.next({
          keyResult: result.keyResult,
          changeId: null,
          objective: result.objective,
        });
        if (result.openNew) {
          this.openAddKeyResultDialog();
        }
      });
  }
}
