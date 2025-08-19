import { Component, inject } from '@angular/core';
import { TeamService } from '../../services/team.service';
import { Team } from '../../shared/types/model/team';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { getRouteToAllTeams, getRouteToTeam } from '../../shared/route-utils';
import { combineLatest } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-team-management-mobile-filter',
  templateUrl: './team-management-mobile-filter.component.html',
  standalone: false
})
export class TeamManagementMobileFilterComponent {
  private readonly teamService = inject(TeamService);

  private readonly router = inject(Router);

  private readonly route = inject(ActivatedRoute);

  readonly ALL_TEAMS = 'alle';

  teams: Team[] = [];

  selectedTeam: Team | undefined | 'alle';

  constructor() {
    combineLatest([this.teamService.getAllTeams(),
      this.route.paramMap])
      .pipe(takeUntilDestroyed())
      .subscribe(([teams,
        params]) => this.setTeamsAndSelectedTeam(teams, params));
  }

  navigate(team: Team | 'alle') {
    team == this.ALL_TEAMS ? this.navigateToAllTeams() : this.navigateToTeam(team);
  }

  private navigateToTeam(team: Team) {
    this.router.navigateByUrl(getRouteToTeam(team.id));
  }

  private navigateToAllTeams() {
    this.router.navigateByUrl(getRouteToAllTeams());
  }

  private setTeamsAndSelectedTeam(teams: Team[], params: ParamMap) {
    this.teams = teams;
    const teamId = params.get('teamId');
    if (teamId) {
      this.selectedTeam = teams.find((t) => t.id === parseInt(teamId));
      return;
    }
    this.selectedTeam = this.ALL_TEAMS;
  }
}
