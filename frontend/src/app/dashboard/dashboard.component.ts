import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Team, TeamService } from '../shared/services/team.service';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Quarter, QuarterService } from '../shared/services/quarter.service';
import { Overview, OverviewService } from '../shared/services/overview.service';
import { RouteService } from '../shared/services/route.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent implements OnInit {
  keyResultForm = new FormGroup({
    teams: new FormControl<string>('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
    ]),
    unit: new FormControl<string>('', [Validators.required]),
    expectedEvolution: new FormControl<string>('', [Validators.required]),
    basicValue: new FormControl<number>({ value: 0, disabled: true }, [
      Validators.required,
    ]),
    targetValue: new FormControl<number>({ value: 0, disabled: true }, [
      Validators.required,
    ]),
    description: new FormControl<string>('', [Validators.maxLength(4096)]),
    ownerId: new FormControl<number | null>(null, [
      Validators.required,
      Validators.nullValidator,
    ]),
  });
  teamList!: Observable<Team[]>;
  teams$: Subject<Overview[]> = new BehaviorSubject<Overview[]>([]);
  quarters$!: Observable<Quarter[]> | undefined;
  teamFilter: number[] = [];
  quarterFilter!: number;

  constructor(
    private teamService: TeamService,
    private quarterService: QuarterService,
    private overviewService: OverviewService,
    private routeService: RouteService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.teamList = this.teamService.getTeams();
    this.quarters$ = this.quarterService.getQuarters();
    this.route.queryParams.subscribe((params) => {});
    this.reloadOverview();
  }

  changeTeamFilter(value: number[]) {
    this.teamFilter = value;
    this.reloadOverview();
  }

  changeQuarterFilter(value: number) {
    this.routeService.changeQuarterFilter(value);
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
