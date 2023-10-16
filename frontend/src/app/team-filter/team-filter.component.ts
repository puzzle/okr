import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, of, Subject } from 'rxjs';
import { Team } from '../shared/types/model/Team';
import { TeamService } from '../shared/services/team.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NotifierService } from '../shared/services/notifier.service';

@Component({
  selector: 'app-team-filter',
  templateUrl: './team-filter.component.html',
  styleUrls: ['./team-filter.component.scss'],
})
export class TeamFilterComponent implements OnInit {
  teams$: BehaviorSubject<Team[]> = new BehaviorSubject<Team[]>([]);
  activeTeams$: BehaviorSubject<number[]> = new BehaviorSubject<number[]>([]);

  constructor(
    private teamService: TeamService,
    private route: ActivatedRoute,
    private router: Router,
    private notifierService: NotifierService,
  ) {}
  ngOnInit(): void {
    this.teamService.getAllTeams().subscribe((teams) => {
      this.teams$.next(teams);
      const teamIds = Array.from(this.route.snapshot.queryParams['teams'])
        .map((id) => Number(id))
        .filter((id) => Number.isInteger(id));
      const selectedTeams = teams.filter((team) => teamIds?.includes(team.id)).map((team) => team.id);
      this.activeTeams$.next(selectedTeams);
    });
  }

  changeTeamFilter() {
    this.router
      .navigate([], { queryParams: { teams: this.activeTeams$.getValue() }, queryParamsHandling: 'merge' })
      .then(() => {
        this.notifierService.reloadOverview.next(null);
      });
  }

  toggleSelection(id: number) {
    const selectedTeam = this.activeTeams$.getValue();
    if (selectedTeam.includes(id)) {
      this.activeTeams$.next(selectedTeam.filter((teamId) => teamId !== id));
    } else {
      selectedTeam.push(id);
      this.activeTeams$.next(selectedTeam);
    }
    this.changeTeamFilter();
  }
}
