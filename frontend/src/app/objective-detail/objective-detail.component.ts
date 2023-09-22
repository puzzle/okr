import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges } from '@angular/core';
import { Objective } from '../shared/types/model/Objective';
import { ObjectiveService } from '../shared/services/objective.service';

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
  ) {}

  ngOnChanges() {
    this.objectiveService.getFullObjective(this.objectiveId).subscribe((fullObjective) => {
      this.objective = fullObjective;
      this.changeDetectorRef.markForCheck();
    });
  }
}
