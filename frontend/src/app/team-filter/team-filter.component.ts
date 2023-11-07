import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Team } from '../shared/types/model/Team';
import { TeamService } from '../shared/services/team.service';
import { ActivatedRoute, Router } from '@angular/router';
import { areEqual, getValueFromQuery, optionalReplaceWithNulls, trackByFn } from '../shared/common';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-team-filter',
  templateUrl: './team-filter.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamFilterComponent implements OnInit {
  teams$: BehaviorSubject<Team[]> = new BehaviorSubject<Team[]>([]);
  activeTeams: number[] = [];
  protected readonly trackByFn = trackByFn;

  constructor(
    private teamService: TeamService,
    private route: ActivatedRoute,
    private router: Router,
    private refreshDataService: RefreshDataService,
  ) {
    this.refreshDataService.reloadOverviewSubject.subscribe(() => {
      this.teamService.getAllTeams().subscribe((teams) => {
        this.teams$.next(teams);
        this.activeTeams = this.activeTeams.filter((teamId) =>
          this.teams$.value.map((team) => team.id).includes(teamId),
        );
      });
    });
  }

  ngOnInit(): void {
    this.teamService.getAllTeams().subscribe((teams: Team[]) => {
      this.teams$.next(teams);
      const teamQuery = this.route.snapshot.queryParams['teams'];
      const teamIds = getValueFromQuery(teamQuery);
      this.activeTeams = teams.filter((team) => teamIds?.includes(team.id)).map((team) => team.id);
      if (this.areAllTeamsShown()) {
        this.activeTeams = [];
      }
      this.changeTeamFilterParams();
    });
  }

  changeTeamFilterParams() {
    const params = { teams: this.activeTeams.join(',') };
    const optionalParams = optionalReplaceWithNulls(params);
    return this.router
      .navigate([], { queryParams: optionalParams })
      .then(() => this.refreshDataService.teamFilterReady.next());
  }

  toggleSelection(id: number) {
    if (this.areAllTeamsShown()) {
      this.activeTeams = this.teams$
        .getValue()
        .filter((team) => team.id != id)
        .map((team) => team.id);
    } else if (this.activeTeams.includes(id)) {
      this.activeTeams = this.activeTeams.filter((teamId) => teamId !== id);
    } else {
      this.activeTeams.push(id);
    }

    if (this.areAllTeamsShown()) {
      this.activeTeams = [];
    }

    this.changeTeamFilterParams();
  }

  areAllTeamsShown() {
    const allTeamsIds = this.teams$.getValue().map((team) => team.id);
    return this.activeTeams.length == 0 || areEqual(this.activeTeams, allTeamsIds);
  }

  getAllObjectivesCount(teams: Team[]) {
    return teams.map((team) => team.activeObjectives).reduce((a, b) => a + b, 0);
  }

  selectAll() {
    if (this.activeTeams.length == 0) {
      return;
    }
    this.activeTeams = [];
    this.changeTeamFilterParams();
  }
}
