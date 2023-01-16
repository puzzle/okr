import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, switchMap } from 'rxjs';
import { Goal, GoalService } from '../../../services/goal.service';
import { Location } from '@angular/common';
import { getNumberOrNull } from '../../../common';

@Component({
  selector: 'app-key-result-overview',
  templateUrl: './key-result-overview.component.html',
  styleUrls: ['./key-result-overview.component.scss'],
})
export class KeyResultOverviewComponent implements OnInit {
  public goal$!: Observable<Goal>;
  public isKeyResultOverview!: boolean;

  constructor(
    private goalService: GoalService,
    private route: ActivatedRoute,
    private location: Location
  ) {}

  ngOnInit(): void {
    window.location.href.includes('measure')
      ? (this.isKeyResultOverview = false)
      : (this.isKeyResultOverview = true);

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

  navigateBack() {
    this.location.back();
  }
}
