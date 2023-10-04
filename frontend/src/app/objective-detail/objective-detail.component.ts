import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { Objective } from '../shared/types/model/Objective';
import { ObjectiveService } from '../shared/services/objective.service';
import { KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { NotifierService } from '../shared/services/notifier.service';
import { catchError, first, Observable, Subject } from 'rxjs';
import { objective } from '../shared/testData';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveDetailComponent implements OnInit {
  @Input()
  objectiveId$!: Observable<number>;

  objective$: Subject<Objective> = new Subject<Objective>();

  constructor(
    private objectiveService: ObjectiveService,
    private dialog: MatDialog,
    private notifierService: NotifierService,
  ) {}

  ngOnInit(): void {
    this.objectiveId$.subscribe((id) => {
      this.loadObjective(id);
    });
  }

  loadObjective(id: number): void {
    console.log('loadObjective with id', id);

    this.objectiveService
      .getFullObjective(id)
      .pipe(
        catchError((err, caught) => {
          console.error(err);
          // TODO: maybe return a EMPTY or NEVER
          return caught;
        }),
      )
      .subscribe((objective) => this.objective$.next(objective));
  }

  openAddKeyResultDialog() {
    // this.objective$.pipe(first()).subscribe((objective) => {
    //   this.dialog
    //     .open(KeyResultDialogComponent, {
    //       width: '45em',
    //       height: 'auto',
    //       data: {
    //         objective: objective,
    //         keyResult: null,
    //       },
    //     })
    //     .afterClosed()
    //     .subscribe(async (result) => {
    //       await this.notifierService.keyResultsChanges.next({
    //         keyResult: result.keyResult,
    //         changeId: null,
    //         objective: result.objective,
    //         delete: false,
    //       });
    //
    //       if (result.openNew) {
    //         this.openAddKeyResultDialog();
    //       }
    //     });
    // });
  }
}
