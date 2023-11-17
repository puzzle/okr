import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { Objective } from '../shared/types/model/Objective';
import { ObjectiveService } from '../shared/services/objective.service';
import { BehaviorSubject, catchError, EMPTY } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { KeyresultDialogComponent } from '../shared/dialog/keyresult-dialog/keyresult-dialog.component';
import { ObjectiveFormComponent } from '../shared/dialog/objective-dialog/objective-form.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveDetailComponent {
  @Input()
  objectiveId!: number;
  objective$: BehaviorSubject<Objective> = new BehaviorSubject<Objective>({} as Objective);

  constructor(
    private objectiveService: ObjectiveService,
    private dialog: MatDialog,
    private refreshDataService: RefreshDataService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadObjective(this.objectiveId);
  }

  loadObjective(id: number): void {
    this.objectiveService
      .getFullObjective(id)
      .pipe(catchError(() => EMPTY))
      .subscribe((objective) => this.objective$.next(objective));
  }

  openAddKeyResultDialog() {
    this.dialog
      .open(KeyresultDialogComponent, {
        width: '45em',
        height: 'auto',
        data: {
          objective: this.objective$.getValue(),
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

  openEditObjectiveDialog() {
    this.dialog
      .open(ObjectiveFormComponent, {
        width: '45em',
        height: 'auto',
        data: {
          objective: {
            objectiveId: this.objective$.getValue().id,
            teamId: this.objective$.value.teamId,
          },
        },
      })
      .afterClosed()
      .subscribe((result) => {
        if (result.delete) {
          this.router.navigate(['/']);
        } else {
          this.loadObjective(this.objective$.value.id);
        }
        this.refreshDataService.markDataRefresh();
      });
  }
}
