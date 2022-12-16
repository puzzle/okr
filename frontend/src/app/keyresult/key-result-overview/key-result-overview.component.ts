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
import { Observable } from 'rxjs';

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
    this.keyResult$ = this.keyResultService.getKeyResultById(
      this.route.snapshot.params['id']
    );
    this.keyResult$.subscribe((keyresult) => {
      this.objective$ = this.objectiveService.getObjectiveById(
        keyresult.objectiveId
      );
    });
  }
}
