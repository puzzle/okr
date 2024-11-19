import { Component } from '@angular/core';
import { TeamService } from '../../services/team.service';
import { Team } from '../../shared/types/model/Team';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { getRouteToAllTeams, getRouteToTeam } from '../../shared/routeUtils';
import { combineLatest } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-team-management-mobile-filter',
  templateUrl: './team-management-mobile-filter.component.html',
})
export class TeamManagementMobileFilterComponent {
  readonly ALL_TEAMS = 'alle';

  teams: Team[] = [];
  selectedTeam: Team | undefined | 'alle';

  constructor(
    private readonly teamService: TeamService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
  ) {
    combineLatest([teamService.getAllTeams(), this.route.paramMap])
      .pipe(takeUntilDestroyed())
      .subscribe(([teams, params]) => this.setTeamsAndSelectedTeam(teams, params));
  }

  navigate(team: Team | 'alle') {
    if (team == this.ALL_TEAMS) this.navigateToAllTeams()
    else this.navigateToTeam(team);
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
