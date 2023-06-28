import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, switchMap } from 'rxjs';
import { getNumberOrNull } from '../../../common';
import { Goal, GoalService } from '../../../services/goal.service';
import { RouteService } from '../../../services/route.service';

@Component({
  selector: 'app-key-result-detail',
  templateUrl: './key-result-detail.component.html',
  styleUrls: ['./key-result-detail.component.scss'],
})
export class KeyResultDetailComponent implements OnInit {
  public goal$!: Observable<Goal>;

  constructor(
    private router: Router,
    private goalService: GoalService,
    private route: ActivatedRoute,
    private routeService: RouteService
  ) {}

  navigateBack() {
    this.routeService.navigate('/dashboard');
  }

  ngOnInit(): void {
    this.goal$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const keyResultId = getNumberOrNull(params.get('keyresultId'));
        if (keyResultId) {
          return this.goalService.getGoalByKeyResultId(keyResultId);
        } else {
          throw Error('Key Result with Id ' + keyResultId + " doesn't exist");
        }
      })
    );
  }
}
