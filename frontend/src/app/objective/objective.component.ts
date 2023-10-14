import { AfterViewInit, ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { MenuEntry } from '../shared/types/menu-entry';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { Router } from '@angular/router';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { MatDialog } from '@angular/material/dialog';
import { KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';
import { BehaviorSubject } from 'rxjs';
import { CloseState } from '../shared/types/enums/CloseState';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { State } from '../shared/types/enums/State';
import { ObjectiveService } from '../shared/services/objective.service';

@Component({
  selector: 'app-objective-column',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveComponent implements AfterViewInit {
  menuEntries: MenuEntry[] = [];

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

  ngAfterViewInit(): void {
    console.log(this.objective.value.state);
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
        { displayName: 'Objective abschliessen', action: 'complete' },
        { displayName: 'Objective freigeben', action: 'release' },
      ];
    }
  }

  redirect(menuEntry: MenuEntry) {
    if (menuEntry.dialog) {
      const matDialogRef = this.matDialog.open(menuEntry.dialog.dialog, {
        data: menuEntry.dialog.data,
        width: '850px',
      });
      matDialogRef.afterClosed().subscribe((result) => {
        this.refreshDataService.markDataRefresh();
      });
    } else {
      if (menuEntry.action) {
        this.objectiveService.getFullObjective(this.objective.value.id).subscribe((objective) => {
          let fullObjective = objective;
          switch (menuEntry.action) {
            case 'reopen':
              fullObjective.state = State.ONGOING;
              break;
            case 'duplicate':
              // TODO Duplicate Objective
              break;
            case 'complete':
              // TODO Open Complete dialog for successful or not successful
              // TODO this.objective.value.state = State.SUCCESSFUL
              // TODO Update Objective with service
              break;
            case 'release':
              fullObjective.state = State.ONGOING;
          }
          this.objectiveService.updateObjective(fullObjective);
        });
      } else {
        this.router.navigate([menuEntry.route!]);
      }
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
