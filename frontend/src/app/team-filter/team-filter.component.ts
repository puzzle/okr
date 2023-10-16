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

  constructor(
    private teamService: TeamService,
    private route: ActivatedRoute,
    private router: Router,
    private notifierService: NotifierService,
  ) {}
  ngOnInit(): void {
    this.teamService.getAllTeams().subscribe((teams) => {
      this.route.queryParams.subscribe((params) => {
        const teamIds = params['teams'];
        const selectedTeams = teams.filter((team) => teamIds?.includes(team.id));
        this.teams$.next(selectedTeams);
      });
    });
  }

  changeTeamFilter() {
    const teamIds = this.teams$.getValue().map((team) => team.id);
    this.router.navigate([], { queryParams: { teams: teamIds }, queryParamsHandling: 'merge' }).then(() => {
      this.notifierService.reloadOverview.next(null);
    });
  }
}
