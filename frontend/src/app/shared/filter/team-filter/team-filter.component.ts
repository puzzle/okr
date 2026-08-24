import { ChangeDetectionStrategy, Component, Input, inject, computed } from '@angular/core';
import { map } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { areEqual, isMobileDevice, optionalReplaceWithNulls } from '../../common';
import { TeamStateService } from '../../../services/team.state.service';
import { Team } from '../../types/model/team';

@Component({
  selector: 'app-team-filter',
  templateUrl: './team-filter.component.html',
  styleUrls: ['./team-filter.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class TeamFilterComponent {
  private readonly teamStateService = inject(TeamStateService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  readonly isMobileDevice = isMobileDevice;

  @Input() minTeams = 0;

  showMoreTeams = true;

  activeTeamIds = toSignal(this.route.queryParams.pipe(map((params): number[] => {
    // TODO: this needs to be updated in the ticket: Query Params Service #1822
    const teamsParam = params['teams'];

    if (!teamsParam) {
      return [];
    }

    const teamArray = Array.isArray(teamsParam) ? teamsParam : teamsParam.split(',');

    return teamArray.map(Number);
  })), { initialValue: [] as number[] });

  rawTeams = this.teamStateService.getTeams();

  teams = computed((): Team[] => {
    if (!isMobileDevice()) {
      return this.rawTeams();
    }

    return this.sortTeamsByActiveStatus(this.activeTeamIds(), this.rawTeams());
  });

  changeTeamFilterParams(newActiveTeams: number[]): void {
    const params = { teams: newActiveTeams.join(',') }; // TODO this needs to be updated in the ticket: reconfigure the router #1824
    const optionalParams = optionalReplaceWithNulls(params);

    this.router.navigate([], {
      queryParams: optionalParams,
      queryParamsHandling: 'merge'
    });
  }

  toggleSelection(id: number): void {
    const currentActive = this.activeTeamIds();
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
    return areEqual(this.activeTeamIds(), this.getAllTeamIds());
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

  sortTeamsByActiveStatus(activeTeamIds: number[], rawTeams: Team[]): Team[] {
    return [...rawTeams].sort((a, b) => {
      const aToggled = activeTeamIds.includes(a.id) ? 0 : 1;
      const bToggled = activeTeamIds.includes(b.id) ? 0 : 1;
      if (aToggled !== bToggled) {
        return aToggled - bToggled;
      }
      return a.name.localeCompare(b.name);
    });
  }
}
