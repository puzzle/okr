import { ChangeDetectionStrategy, Component, Input, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, filter, Subject, Subscription, takeUntil } from 'rxjs';
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
  standalone: false
})
export class TeamFilterComponent implements OnInit, OnDestroy {
  teams$: BehaviorSubject<Team[]> = new BehaviorSubject<Team[]>([]);

  @Input() minTeams = 0;

  activeTeams: number[] = [];

  protected readonly trackByFn = trackByFn;

  private unsubscribe$ = new Subject<void>();

  private subscription?: Subscription;

  showMoreTeams = true;

  isMobile = false;

  constructor(
    private teamService: TeamService,
    private route: ActivatedRoute,
    private router: Router,
    private refreshDataService: RefreshDataService,
    private userService: UserService,
    private breakpointObserver: BreakpointObserver
  ) {
    this.refreshDataService.reloadOverviewSubject.pipe(takeUntil(this.unsubscribe$))
      .subscribe(() => {
        this.refreshTeamData();
      });
  }

  ngOnInit(): void {
    this.refreshTeamData();

    this.breakpointObserver
      .observe(['(min-width: 1px) and (max-width: 767px)'])
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((result) => {
        this.isMobile = result.matches;
      });
  }

  private refreshTeamData() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.subscription = this.teamService
      .getAllTeams()
      .pipe(takeUntil(this.unsubscribe$), filter((teams) => teams.length > 0))
      .subscribe((teams: Team[]) => {
        this.teams$.next(teams);
        const teamQuery = this.route.snapshot.queryParams['teams'];
        const teamIds = getValueFromQuery(teamQuery);
        const knownTeams = this.getAllTeamIds()
          .filter((teamId) => teamIds?.includes(teamId));
        if (knownTeams.length == 0) {
          this.activeTeams = extractTeamsFromUser(this.userService.getCurrentUser())
            .map((team) => team.id);
        } else {
          this.activeTeams = knownTeams;
        }
        if (this.isMobile) {
          this.teams$.next(this.sortTeamsToggledPriority());
        }
        this.changeTeamFilterParams();
      });
  }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  changeTeamFilterParams() {
    const params = { teams: this.activeTeams.join(',') };
    const optionalParams = optionalReplaceWithNulls(params);
    this.router
      .navigate([], { queryParams: optionalParams })
      .then(() => this.refreshDataService.teamFilterReady.next());
  }

  toggleSelection(id: number) {
    if (this.areAllTeamsShown()) {
      this.activeTeams = [id];
    } else if (this.activeTeams.includes(id)) {
      if (this.activeTeams.length == this.minTeams) {
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

  areAllTeamsShown() {
    return areEqual(this.activeTeams, this.getAllTeamIds());
  }

  toggleAll() {
    if (this.areAllTeamsShown() && this.minTeams > 0) {
      return;
    }
    this.activeTeams = this.areAllTeamsShown() ? [] : this.getAllTeamIds();
    this.changeTeamFilterParams();
  }

  getAllTeamIds() {
    return this.teams$.getValue()
      .map((team) => team.id);
  }

  getTeamName(id: number): string {
    const teamName = this.teams$.getValue()
      .find((team) => team.id === id)?.name;
    return teamName ?? 'no team name';
  }

  sortTeamsToggledPriority() {
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
