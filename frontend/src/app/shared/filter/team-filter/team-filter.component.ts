import { ChangeDetectionStrategy, Component, Input, inject, computed, effect } from '@angular/core';
import { map } from 'rxjs';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { areEqual, optionalReplaceWithNulls } from '../../common';
import { RefreshDataService } from '../../../services/refresh-data.service';
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

  private breakpointObserver = inject(BreakpointObserver);

  @Input() minTeams = 0;

  showMoreTeams = true;

  activeTeams = toSignal(this.route.queryParams.pipe(map((params) => {
    const teamsParam = params['teams'];

    if (!teamsParam) {
      return [];
    }

    const teamArray = Array.isArray(teamsParam) ? teamsParam : teamsParam.split(',');

    return teamArray.map(Number);
  })), { initialValue: [] as number[] });

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

  constructor() {
    this.refreshDataService.reloadOverviewSubject
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.teamStateService.reload());

    effect(() => {
      if (this.activeTeams().length > 0) {
        this.refreshDataService.teamFilterReady.next();
      }
    });
  }

  changeTeamFilterParams(newActiveTeams: number[]): void {
    const params = { teams: newActiveTeams.join(',') };
    const optionalParams = optionalReplaceWithNulls(params);

    this.router.navigate([], {
      queryParams: optionalParams,
      queryParamsHandling: 'merge'
    })
      .then(() => this.refreshDataService.teamFilterReady.next());
  }

  toggleSelection(id: number): void {
    const currentActive = this.activeTeams();
    let nextActive: number[];

    if (this.areAllTeamsShown()) {
      nextActive = [id];
    } else if (currentActive.includes(id)) {
      if (currentActive.length === this.minTeams) {
        return;
      }
      nextActive = currentActive.filter((teamId: number) => teamId !== id);
    } else {
      nextActive = [...currentActive,
        id];
    }

    this.changeTeamFilterParams(nextActive);
  }

  areAllTeamsShown(): boolean {
    return areEqual(this.activeTeams(), this.getAllTeamIds());
  }

  toggleAll(): void {
    if (this.areAllTeamsShown() && this.minTeams > 0) {
      return;
    }
    const nextActive = this.areAllTeamsShown() ? [] : this.getAllTeamIds();
    this.changeTeamFilterParams(nextActive);
  }

  getAllTeamIds(): number[] {
    return this.rawTeams()
      .map((team) => team.id);
  }
}
