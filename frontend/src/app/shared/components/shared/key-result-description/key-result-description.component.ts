import { Component, OnInit } from '@angular/core';
import { Observable, switchMap } from 'rxjs';
import { Goal, GoalService } from '../../../services/goal.service';
import { ActivatedRoute } from '@angular/router';
import { getNumberOrNull } from '../../../common';

@Component({
  selector: 'app-key-result-description',
  templateUrl: './key-result-description.component.html',
  styleUrls: ['./key-result-description.component.scss'],
})
export class KeyResultDescriptionComponent implements OnInit {
  public goal$!: Observable<Goal>;

  constructor(
    private goalService: GoalService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.goal$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const keyResultId = getNumberOrNull(params.get('keyresultId'));
        if (keyResultId) {
          return this.goalService.getGoalByKeyResultId(keyResultId);
        } else {
          throw Error('KeyResult with Id ' + keyResultId + " doesn't exist");
        }
      })
    );
  }
}
