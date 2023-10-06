import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, AfterViewInit } from '@angular/core';
import { Objective } from '../shared/types/model/Objective';
import { ObjectiveService } from '../shared/services/objective.service';
import { KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { NotifierService } from '../shared/services/notifier.service';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveDetailComponent implements AfterViewInit {
  @Input() objectiveId!: number;
  objective!: Objective;

  constructor(
    private objectiveService: ObjectiveService,
    private changeDetectorRef: ChangeDetectorRef,
    private dialog: MatDialog,
    private notifierService: NotifierService,
  ) {}

  ngAfterViewInit(): void {
    this.objectiveService.getFullObjective(this.objectiveId).subscribe((fullObjective) => {
      this.objective = fullObjective;
      this.changeDetectorRef.markForCheck();
    });
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
  closeDrawer() {
    this.notifierService.closeDetailSubject.next();
  }
}
