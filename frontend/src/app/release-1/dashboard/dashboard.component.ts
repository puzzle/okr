import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Team, TeamService } from '../shared/services/team.service';
import {
  BehaviorSubject,
  combineLatest,
  debounceTime,
  distinctUntilChanged,
  fromEvent,
  map,
  Observable,
  Subject,
  takeUntil,
} from 'rxjs';
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
export class DashboardComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  filters = new FormGroup({
    teamsFilter: new FormControl<number[]>([]),
    quarterFilter: new FormControl<string>(''),
  });
  teams$!: Observable<Team[]>;
  overview$: Subject<Overview[]> = new BehaviorSubject<Overview[]>([]);
  quarters$!: Observable<Quarter[]> | undefined;

  private scrollPosition = 0;
  private readonly storageKey = 'dashboardScrollPosition';

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

    combineLatest([this.quarters$, this.route.queryParams])
      .pipe(
        takeUntil(this.destroy$),
        map(([quarters, queryParams]) => {
          if (queryParams['quarterFilter']) {
            this.filters.controls.quarterFilter.setValue(queryParams['quarterFilter']);
          } else if (quarters.length) {
            this.filters.controls.quarterFilter.setValue(quarters[0].id.toString());
            this.changeQuarterFilter(quarters[0].id);
          }
        })
      )
      .subscribe();
    this.route.queryParams
      .pipe(
        map((params) => {
          let selectedTeams: number[] = [];
          (params['teamFilter']?.split(',') ?? []).forEach((item: string) =>
            selectedTeams.push(getNumberOrNull(item)!)
          );
          this.filters.controls.teamsFilter.setValue(selectedTeams);
          this.changeTeamFilter(selectedTeams);
        })
      )
      .subscribe();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  ngAfterViewChecked() {
    const storedPosition = sessionStorage.getItem(this.storageKey);
    if (storedPosition) {
      this.scrollPosition = parseInt(storedPosition);
      window.scrollTo(0, this.scrollPosition);
    }
    // Listen to the scroll event using RxJS
    const scroll$ = fromEvent(window, 'scroll');
    // Debounce the scroll event and subscribe to it
    scroll$.pipe(debounceTime(500), distinctUntilChanged(), takeUntil(this.destroy$)).subscribe(() => {
      this.saveScrollPosition();
    });
  }

  saveScrollPosition() {
    sessionStorage.setItem(this.storageKey, window.scrollY.toString());
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

  matSelectCompareQuarter(quarterId: number, label: string): boolean {
    return quarterId.toString() === label;
  }

  reloadOverview() {
    this.route.queryParams
      .subscribe((params) => {
        this.overviewService.getOverview(params['quarterFilter'], params['teamFilter'] ?? []).subscribe((data) => {
          this.overview$.next(data);
        });
      })
      .unsubscribe();
  }
}
