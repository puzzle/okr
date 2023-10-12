import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { Objective } from '../shared/types/model/Objective';
import { ObjectiveService } from '../shared/services/objective.service';
import { catchError, EMPTY, Observable, ReplaySubject, Subject } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveDetailComponent {
  @Input()
  objectiveId$!: Observable<number>;
  objective$: Subject<Objective> = new ReplaySubject<Objective>(1);

  constructor(
    private objectiveService: ObjectiveService,
    private dialog: MatDialog,
    private refreshDataService: RefreshDataService,
  ) {}

  ngOnInit(): void {
    this.objectiveId$.subscribe((id) => {
      this.loadObjective(id);
    });
  }

  loadObjective(id: number): void {
    this.objectiveService
      .getFullObjective(id)
      .pipe(
        catchError((err, caught) => {
          return EMPTY;
        }),
      )
      .subscribe((objective) => this.objective$.next(objective));
  }

  openAddKeyResultDialog() {
    this.dialog
      .open(KeyResultDialogComponent, {
        width: '45em',
        height: 'auto',
        data: {
          objective: this.objective$,
          keyResult: null,
        },
      })
      .afterClosed()
      .subscribe(async (result) => {
        if (result && result.openNew) {
          this.openAddKeyResultDialog();
        }
        this.refreshDataService.markDataRefresh();
      });
  }
}
