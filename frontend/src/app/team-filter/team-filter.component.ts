import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Team } from '../shared/types/model/Team';
import { TeamService } from '../shared/services/team.service';
import { ActivatedRoute, Router } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { getValueFromQuery } from '../shared/common';

@Component({
  selector: 'app-team-filter',
  templateUrl: './team-filter.component.html',
  styleUrls: ['./team-filter.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamFilterComponent implements OnInit {
  teams$: BehaviorSubject<Team[]> = new BehaviorSubject<Team[]>([]);
  activeTeams: number[] = [];

  constructor(
    private teamService: TeamService,
    private route: ActivatedRoute,
    private router: Router,
    private refreshDataService: RefreshDataService,
  ) {}

  ngOnInit(): void {
    this.teamService.getAllTeams().subscribe((teams) => {
      this.teams$.next(teams);
      const teamQuery = this.route.snapshot.queryParams['teams'];
      const teamIds = getValueFromQuery(teamQuery);
      this.activeTeams = teams.filter((team) => teamIds?.includes(team.id)).map((team) => team.id);
      this.clearIfAllTeamsAreSelected();
      this.changeTeamFilter();
    });
  }

  changeTeamFilter() {
    this.router.navigate([], { queryParams: { teams: this.activeTeams }, queryParamsHandling: 'merge' }).then(() => {
      this.refreshDataService.markDataRefresh();
    });
  }

  toggleSelection(id: number) {
    if (this.activeTeams.includes(id)) {
      this.activeTeams = this.activeTeams.filter((teamId) => teamId !== id);
    } else {
      this.activeTeams.push(id);
    }
    this.clearIfAllTeamsAreSelected();
    this.changeTeamFilter();
  }

  clearIfAllTeamsAreSelected() {
    if (
      this.activeTeams.sort().toString() ==
      this.teams$
        .getValue()
        .map((team) => team.id)
        .sort()
        .toString()
    ) {
      this.activeTeams = [];
    }
  }

  getAllObjectives(teams: Team[]) {
    return teams.map((team) => team.activeObjectives).reduce((a, b) => a + b, 0);
  }

  selectAll() {
    this.activeTeams = [];
    this.changeTeamFilter();
  }
}
