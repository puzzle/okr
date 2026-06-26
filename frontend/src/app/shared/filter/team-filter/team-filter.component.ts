import { ChangeDetectionStrategy, Component, Input, inject, signal, computed } from '@angular/core';
import { map, filter } from 'rxjs';
import { takeUntilDestroyed, toSignal, toObservable } from '@angular/core/rxjs-interop';
import { Team } from '../../types/model/team';
import { ActivatedRoute, Router } from '@angular/router';
import { areEqual, getValueFromQuery, optionalReplaceWithNulls } from '../../common';
import { RefreshDataService } from '../../../services/refresh-data.service';
import { UserService } from '../../../services/user.service';
import { extractTeamsFromUser } from '../../types/model/user';
import { BreakpointObserver } from '@angular/cdk/layout';
import { TeamStateService } from '../../../services/team.state.service';

@Component({
  selector: 'app-team-filter',
  templateUrl: './team-filter.component.html',
  styleUrls: ['./team-filter.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class TeamFilterComponent {
  private readonly teamStateService = inject(TeamStateService);

  private route = inject(ActivatedRoute);

  private router = inject(Router);

  private refreshDataService = inject(RefreshDataService);

  private userService = inject(UserService);

  private breakpointObserver = inject(BreakpointObserver);

  @Input() minTeams = 0;

  showMoreTeams = true;

  private isInitialLoad = true;

  activeTeams = signal<number[]>([]);

  isMobile = toSignal(this.breakpointObserver.observe(['(min-width: 1px) and (max-width: 767px)'])
    .pipe(map((result) => result.matches)), { initialValue: false });

  rawTeams = computed(() => {
    return this.teamStateService.getTeams()()
      .filter((team) => !team.markedAsArchivedAt);
  });

  teams = computed(() => {
    const t = this.rawTeams();
    if (!this.isMobile()) {
      return t;
    }

    const active = this.activeTeams();
    return [...t].sort((a, b) => {
      const aToggled = active.includes(a.id) ? 0 : 1;
      const bToggled = active.includes(b.id) ? 0 : 1;
      if (aToggled !== bToggled) {
        return aToggled - bToggled;
      }
      return a.name.localeCompare(b.name);
    });
  });

  constructor() {
    this.refreshDataService.reloadOverviewSubject
      .pipe(takeUntilDestroyed())
      .subscribe(() => {
        this.teamStateService.reload();
      });

    toObservable(this.rawTeams)
      .pipe(filter((teams) => teams.length > 0), takeUntilDestroyed())
      .subscribe((teams) => {
        this.processIncomingTeams(teams);
      });
  }

  private processIncomingTeams(teams: Team[]): void {
    const teamQuery = this.route.snapshot.queryParams['teams'];
    const teamIds = getValueFromQuery(teamQuery);
    const knownTeams = teams.map((t) => t.id)
      .filter((id) => teamIds?.includes(id));

    const isInitialLoadWithoutParam = this.isInitialLoad && teamQuery === undefined;
    const hasQueryButNoKnownTeams = teamQuery !== undefined && knownTeams.length === 0;

    if (isInitialLoadWithoutParam || hasQueryButNoKnownTeams) {
      this.activeTeams.set(extractTeamsFromUser(this.userService.getCurrentUser())
        .map((t) => t.id));
    } else {
      this.activeTeams.set(knownTeams);
    }

    this.isInitialLoad = false;
    this.changeTeamFilterParams();
  }

  changeTeamFilterParams(): void {
    const params = { teams: this.activeTeams()
      .join(',') };
    const optionalParams = optionalReplaceWithNulls(params);

    this.router
      .navigate([], { queryParams: optionalParams,
        queryParamsHandling: 'merge' })
      .then(() => this.refreshDataService.teamFilterReady.next());
  }

  toggleSelection(id: number): void {
    const currentActive = this.activeTeams();
    if (this.areAllTeamsShown()) {
      this.activeTeams.set([id]);
    } else if (currentActive.includes(id)) {
      if (currentActive.length === this.minTeams) {
        return;
      }
      this.activeTeams.set(currentActive.filter((teamId) => teamId !== id));
    } else {
      this.activeTeams.set([...currentActive,
        id]);
    }
    this.changeTeamFilterParams();
  }

  areAllTeamsShown(): boolean {
    return areEqual(this.activeTeams(), this.getAllTeamIds());
  }

  toggleAll(): void {
    if (this.areAllTeamsShown() && this.minTeams > 0) {
      return;
    }
    this.activeTeams.set(this.areAllTeamsShown() ? [] : this.getAllTeamIds());
    this.changeTeamFilterParams();
  }

  getAllTeamIds(): number[] {
    return this.rawTeams()
      .map((team) => team.id);
  }

  getTeamName(id: number): string {
    return this.teams()
      .find((team) => team.id === id)?.name ?? 'no team name';
  }
}
