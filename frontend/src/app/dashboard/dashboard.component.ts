import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Team, TeamService } from '../shared/services/team.service';
import { BehaviorSubject, first, Observable, Subject } from 'rxjs';
import { Quarter, QuarterService } from '../shared/services/quarter.service';
import { Overview, OverviewService } from '../shared/services/overview.service';
import { RouteService } from '../shared/services/route.service';
import { ActivatedRoute } from '@angular/router';
import { getNumberOrNull } from '../shared/common';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent implements OnInit {
  filters = new FormGroup({
    teamsFilter: new FormControl<number[]>([]),
    quarterFilter: new FormControl<number>(0),
  });
  teams$!: Observable<Team[]>;
  overview$: Subject<Overview[]> = new BehaviorSubject<Overview[]>([]);
  quarters$!: Observable<Quarter[]> | undefined;

  constructor(
    private teamService: TeamService,
    private quarterService: QuarterService,
    private overviewService: OverviewService,
    private routeService: RouteService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.teams$ = this.teamService.getTeams();
    this.quarters$ = this.quarterService.getQuarters();
    //select filter values from url
    this.route.queryParams
      .subscribe((params) => {
        let selectedTeams: number[] = [];
        (params['teamFilter']?.split(',') ?? []).forEach((item: string) =>
          selectedTeams.push(getNumberOrNull(item)!)
        );
        this.filters.setValue({
          quarterFilter: getNumberOrNull(params['quarterFilter']),
          teamsFilter: selectedTeams,
        });
      })
      .unsubscribe();
    this.reloadOverview();
  }

  changeTeamFilter(value: number[]) {
    this.routeService.changeTeamFilter(value).subscribe(() => {
      this.reloadOverview();
    });
  }

  changeQuarterFilter(value: number) {
    this.routeService.changeQuarterFilter(value).subscribe(() => {
      this.reloadOverview();
    });
  }

  reloadOverview() {
    this.route.queryParams
      .subscribe((params) => {
        this.overviewService
          .getOverview(params['quarterFilter'], params['teamFilter'] ?? [])
          .subscribe((data) => {
            this.overview$.next(data);
          });
      })
      .unsubscribe();
  }
}
