import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Team, TeamService } from '../shared/services/team.service';
import { Observable } from 'rxjs';
import { Quarter, QuarterService } from '../shared/services/quarter.service';
import { Overview, OverviewService } from '../shared/services/overview.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent implements OnInit {
  teams = new FormControl('');
  teamList!: Observable<Team[]>;
  teams$!: Observable<Overview[]>;
  quarters$!: Observable<Quarter[]> | undefined;
  teamFilter: number[] = [];
  quarterFilter!: number;

  constructor(
    private teamService: TeamService,
    private quarterService: QuarterService,
    private overviewService: OverviewService
  ) {}

  ngOnInit(): void {
    this.teamList = this.teamService.getTeams();
    this.teams$ = this.overviewService.getOverview(
      this.quarterFilter,
      this.teamFilter
    );
    this.quarters$ = this.quarterService.getQuarters();
  }

  changeTeamFilter(value: number[]) {
    this.teamFilter = value;
    this.teams$ = this.overviewService.getOverview(
      this.quarterFilter,
      this.teamFilter
    );
  }

  changeQuarterFilter(value: number) {
    this.quarterFilter = value;
    this.teams$ = this.overviewService.getOverview(
      this.quarterFilter,
      this.teamFilter
    );
  }
}
