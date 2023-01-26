import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Team, TeamService } from '../shared/services/team.service';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
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
  teams$: Subject<Overview[]> = new BehaviorSubject<Overview[]>([]);
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
    this.quarters$ = this.quarterService.getQuarters();
    this.reloadOverview();
  }

  changeTeamFilter(value: number[]) {
    this.teamFilter = value;
    this.reloadOverview();
  }

  changeQuarterFilter(value: number) {
    this.quarterFilter = value;
    this.reloadOverview();
  }

  reloadOverview() {
    this.overviewService
      .getOverview(this.quarterFilter, this.teamFilter)
      .subscribe((data) => {
        this.teams$.next(data);
      });
  }
}
