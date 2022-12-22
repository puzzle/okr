import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { Goal, GoalService } from '../../shared/services/goal.service';

@Component({
  selector: 'app-key-result-overview',
  templateUrl: './key-result-overview.component.html',
  styleUrls: ['./key-result-overview.component.scss'],
})
export class KeyResultOverviewComponent implements OnInit {
  public goal$!: Observable<Goal>;

  constructor(
    private goalService: GoalService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.goal$ = this.goalService.getGoalByKeyResultId(
      this.route.snapshot.params['id']
    );
  }
}
