import { ChangeDetectionStrategy, Component, Input, OnDestroy, OnInit, inject } from '@angular/core';
import { BehaviorSubject, filter, Subject, distinctUntilChanged, map, takeUntil } from 'rxjs';
import { Team } from '../../types/model/team';
import { TeamService } from '../../../services/team.service';
import { ActivatedRoute, Router } from '@angular/router';
import { areEqual, getValueFromQuery, optionalReplaceWithNulls, trackByFn } from '../../common';
import { RefreshDataService } from '../../../services/refresh-data.service';
import { UserService } from '../../../services/user.service';
import { extractTeamsFromUser } from '../../types/model/user';
import { BreakpointObserver } from '@angular/cdk/layout';

@Component({
  selector: 'app-team-filter',
  templateUrl: './team-filter.component.html',
  styleUrls: ['./team-filter.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false,
  providers: [TeamService]
})
export class TeamFilterComponent implements OnInit, OnDestroy {
  private teamService = inject(TeamService);

  private route = inject(ActivatedRoute);

  private router = inject(Router);

  private refreshDataService = inject(RefreshDataService);

  private userService = inject(UserService);

  private breakpointObserver = inject(BreakpointObserver);

  teams$: BehaviorSubject<Team[]> = new BehaviorSubject<Team[]>([]);

  @Input() minTeams = 0;

  activeTeams: number[] = [];

  showMoreTeams = true;

  isMobile = false;

  protected readonly trackByFn = trackByFn;

  private unsubscribe$ = new Subject<void>();

  private isInitialLoad = true;

  constructor() {
    this.refreshDataService.reloadOverviewSubject
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(() => {
        this.teamService.reload();
      });
  }

  ngOnInit(): void {
    this.isInitialLoad = true;

    this.route.queryParams
      .pipe(map((params) => (params['quarter'] ? Number(params['quarter']) : undefined)), distinctUntilChanged(), takeUntil(this.unsubscribe$))
      .subscribe((quarterId) => {
        this.teamService.loadTeams({ quarterId });
      });

    this.teamService.getTeams()
      .pipe(filter((teams) => teams.length > 0), takeUntil(this.unsubscribe$))
      .subscribe((teams: Team[]) => {
        this.processIncomingTeams(teams);
      });

    this.breakpointObserver
      .observe(['(min-width: 1px) and (max-width: 767px)'])
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((result) => {
        this.isMobile = result.matches;
        if (this.isMobile) {
          this.teams$.next(this.sortTeamsToggledPriority());
        }
      });
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  private processIncomingTeams(teams: Team[]): void {
    this.teams$.next(teams);

    const teamQuery = this.route.snapshot.queryParams['teams'];
    const teamIds = getValueFromQuery(teamQuery);
    const knownTeams = this.getAllTeamIds()
      .filter((teamId) => teamIds?.includes(teamId));

    const isInitialLoadWithoutParam = this.isInitialLoad && teamQuery === undefined;
    const hasQueryButNoKnownTeams = teamQuery !== undefined && knownTeams.length === 0;

    if (isInitialLoadWithoutParam || hasQueryButNoKnownTeams) {
      this.activeTeams = extractTeamsFromUser(this.userService.getCurrentUser())
        .map((t) => t.id);
    } else {
      this.activeTeams = knownTeams;
    }

    this.isInitialLoad = false;

    if (this.isMobile) {
      this.teams$.next(this.sortTeamsToggledPriority());
    }
    this.changeTeamFilterParams();
  }

  changeTeamFilterParams(): void {
    const params = { teams: this.activeTeams.join(',') };
    const optionalParams = optionalReplaceWithNulls(params);

    this.router
      .navigate([], { queryParams: optionalParams,
        queryParamsHandling: 'merge' })
      .then(() => this.refreshDataService.teamFilterReady.next());
  }

  toggleSelection(id: number): void {
    if (this.areAllTeamsShown()) {
      this.activeTeams = [id];
    } else if (this.activeTeams.includes(id)) {
      if (this.activeTeams.length === this.minTeams) {
        return;
      }
      this.activeTeams = this.activeTeams.filter((teamId) => teamId !== id);
    } else {
      this.activeTeams.push(id);
    }

    if (this.isMobile) {
      this.teams$.next(this.sortTeamsToggledPriority());
    }
    this.changeTeamFilterParams();
  }

  areAllTeamsShown(): boolean {
    return areEqual(this.activeTeams, this.getAllTeamIds());
  }

  toggleAll(): void {
    if (this.areAllTeamsShown() && this.minTeams > 0) {
      return;
    }
    this.activeTeams = this.areAllTeamsShown() ? [] : this.getAllTeamIds();
    this.changeTeamFilterParams();
  }

  getAllTeamIds(): number[] {
    return this.teams$.getValue()
      .map((team) => team.id);
  }

  getTeamName(id: number): string {
    const teamName = this.teams$.getValue()
      .find((team) => team.id === id)?.name;
    return teamName ?? 'no team name';
  }

  sortTeamsToggledPriority(): Team[] {
    return this.teams$.getValue()
      .sort((a, b) => {
        const aToggled = this.activeTeams.includes(a.id) ? 0 : 1;
        const bToggled = this.activeTeams.includes(b.id) ? 0 : 1;

        if (aToggled !== bToggled) {
          return aToggled - bToggled;
        }
        return a.name.localeCompare(b.name);
      });
  }
}
