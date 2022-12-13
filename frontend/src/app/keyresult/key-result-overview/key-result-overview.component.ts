import { Component, OnInit } from '@angular/core';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../shared/services/key-result.service';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
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
  public keyResult$!: Observable<KeyResultMeasure>;
  public objective$!: Observable<Objective>;

  constructor(
    private keyResultService: KeyResultService,
    private objectiveService: ObjectiveService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.keyResult$ = this.keyResultService.getKeyResultById(params['id']);
    });
    this.keyResult$.subscribe(
      (key_result) =>
        (this.objective$ = this.objectiveService.getObjectiveById(
          key_result.objectiveId
        ))
    );
  }
}
