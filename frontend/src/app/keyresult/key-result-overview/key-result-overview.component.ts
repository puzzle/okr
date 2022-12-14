import { Component, OnInit } from '@angular/core';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../shared/services/key-result.service';
import { ActivatedRoute } from '@angular/router';
import {
  Objective,
  ObjectiveService,
} from '../../shared/services/objective.service';

@Component({
  selector: 'app-key-result-overview',
  templateUrl: './key-result-overview.component.html',
  styleUrls: ['./key-result-overview.component.scss'],
})
export class KeyResultOverviewComponent implements OnInit {
  public keyResult!: KeyResultMeasure;
  public objective!: Objective;

  constructor(
    private keyResultService: KeyResultService,
    private objectiveService: ObjectiveService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.keyResultService
      .getKeyResultById(this.route.snapshot.params['id'])
      .subscribe((keyResult) => {
        this.keyResult = keyResult;
        this.objectiveService
          .getObjectiveById(keyResult.objectiveId)
          .subscribe((objective) => {
            this.objective = objective;
          });
      });
  }
}
