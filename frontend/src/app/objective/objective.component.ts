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

@Component({
  selector: 'app-objective-column',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveComponent implements AfterViewInit {
  menuEntries: MenuEntry[] = [];
  private objective$ = new BehaviorSubject<ObjectiveMin>({} as ObjectiveMin);

  constructor(
    private matDialog: MatDialog,
    private router: Router,
    private refreshDataService: RefreshDataService,
  ) {}

  @Input()
  get objective(): BehaviorSubject<ObjectiveMin> {
    return this.objective$;
  }

  set objective(objective: ObjectiveMin) {
    this.objective$.next(objective);
  }

  ngAfterViewInit(): void {
    this.menuEntries = [
      {
        displayName: 'Objective bearbeiten',
        dialog: { dialog: ObjectiveFormComponent, data: { objectiveId: this.objective.value.id } },
      },
      { displayName: 'Objective duplizieren' },
      { displayName: 'Objective abschliessen' },
      { displayName: 'Objective freigeben' },
    ];
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
