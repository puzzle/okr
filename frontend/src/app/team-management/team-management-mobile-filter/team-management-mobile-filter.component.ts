import { Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { Team } from '../../shared/types/model/team';
import { ActivatedRoute, Router } from '@angular/router';
import { getRouteToAllTeams, getRouteToTeam } from '../../shared/route-utils';
import { ALL_TEAMS_STATE } from '../../services/team-state.tokens';

@Component({
  selector: 'app-team-management-mobile-filter',
  templateUrl: './team-management-mobile-filter.component.html',
  standalone: false
})
export class TeamManagementMobileFilterComponent {
  private readonly teamStateService = inject(ALL_TEAMS_STATE);

  private readonly router = inject(Router);

  private readonly route = inject(ActivatedRoute);

  readonly ALL_TEAMS = 'alle';

  private readonly paramMap = toSignal(this.route.paramMap);

  readonly teams = this.teamStateService.getTeams();

  readonly selectedTeam = computed(() => {
    const teamId = this.paramMap()
      ?.get('teamId');
    const teams = this.teams();

    if (teamId) {
      return teams.find((t) => t.id === parseInt(teamId));
    }
    return this.ALL_TEAMS;
  });

  navigate(team: Team | 'alle') {
    team === this.ALL_TEAMS ? this.navigateToAllTeams() : this.navigateToTeam(team as Team);
  }

  private navigateToTeam(team: Team) {
    this.router.navigateByUrl(getRouteToTeam(team.id));
  }

  private navigateToAllTeams() {
    this.router.navigateByUrl(getRouteToAllTeams());
  }
}
