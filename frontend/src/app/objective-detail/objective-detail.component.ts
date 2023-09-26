import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges } from '@angular/core';
import { Objective } from '../shared/types/model/Objective';
import { ObjectiveService } from '../shared/services/objective.service';
import { KeyResultDialogComponent } from '../key-result-dialog/key-result-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveDetailComponent implements OnChanges {
  @Input() objectiveId!: number;
  objective!: Objective;

  constructor(
    private objectiveService: ObjectiveService,
    private changeDetectorRef: ChangeDetectorRef,
    private dialog: MatDialog,
  ) {}

  ngOnChanges() {
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
      .subscribe((result) => {
        if (result == 'openNewDialog') {
          this.openAddKeyResultDialog();
        }
      });
  }
}
