import { ChangeDetectionStrategy, Component, Input, inject, signal, computed } from '@angular/core';
import { map, filter } from 'rxjs';
import { takeUntilDestroyed, toSignal, toObservable } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { areEqual, getValueFromQuery, optionalReplaceWithNulls } from '../../common';
import { RefreshDataService } from '../../../services/refresh-data.service';
import { UserService } from '../../../services/user.service';
import { extractTeamsFromUser } from '../../types/model/user';
import { BreakpointObserver } from '@angular/cdk/layout';
import { TeamStateService } from '../../../services/team.state.service';
import { QuarterService } from '../../../services/quarter.service';

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

  private quarterService = inject(QuarterService);

  private breakpointObserver = inject(BreakpointObserver);

  @Input() minTeams = 0;

  showMoreTeams = true;

  readonly quarters = toSignal(this.quarterService.getAllQuarters(), { initialValue: [] });

  activeTeams = signal<number[]>([]);

  isMobile = toSignal(this.breakpointObserver.observe(['(min-width: 1px) and (max-width: 767px)'])
    .pipe(map((result) => result.matches)), { initialValue: false });

  rawTeams = this.teamStateService.getTeams();

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

  private isInitialized = false;

  constructor() {
    this.refreshDataService.reloadOverviewSubject
      .pipe(takeUntilDestroyed())
      .subscribe(() => {
        this.teamStateService.reload();
      });

    toObservable(this.rawTeams)
      .pipe(filter((teams) => teams.length > 0), takeUntilDestroyed())
      .subscribe((teams) => {
        if (!this.isInitialized) {
          const teamQuery = this.route.snapshot.queryParams['teams'];
          const teamIds = getValueFromQuery(teamQuery);

          const knownTeams = teams.map((t) => t.id)
            .filter((id) => teamIds?.includes(id));

          if (knownTeams.length > 0) {
            this.activeTeams.set(knownTeams);
          } else {
            const userTeams = extractTeamsFromUser(this.userService.getCurrentUser());

            const validUserTeamIds = userTeams
              .map((team) => team.id)
              .filter((id) => teams.some((validTeam) => validTeam.id === id));

            this.activeTeams.set(validUserTeamIds);
          }

          this.isInitialized = true;
          this.changeTeamFilterParams();
        } else {
          const currentActive = this.activeTeams();
          const validActive = currentActive.filter((id) => teams.some((t) => t.id === id));

          if (currentActive.length !== validActive.length) {
            this.activeTeams.set(validActive);
            this.changeTeamFilterParams();
          }
        }
      });
  }

  changeTeamFilterParams(): void {
    const params = { teams: this.activeTeams()
      .join(',') };
    const optionalParams = optionalReplaceWithNulls(params);

    setTimeout(() => this.router.navigate([], { queryParams: optionalParams }), 100);
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
}
