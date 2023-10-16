import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Team } from '../shared/types/model/Team';
import { TeamService } from '../shared/services/team.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NotifierService } from '../shared/services/notifier.service';

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
    private notifierService: NotifierService,
  ) {}
  ngOnInit(): void {
    this.teamService.getAllTeams().subscribe((teams) => {
      this.teams$.next(teams);
      const teamQuery = this.route.snapshot.queryParams['teams'];
      const teamIds = this.teamService.getTeamIdsFromQuery(teamQuery);
      this.activeTeams = teams.filter((team) => teamIds?.includes(team.id)).map((team) => team.id);
    });
  }

  changeTeamFilter() {
    this.router.navigate([], { queryParams: { teams: this.activeTeams }, queryParamsHandling: 'merge' }).then(() => {
      this.notifierService.reloadOverview.next(null);
    });
  }

  toggleSelection(id: number) {
    if (this.activeTeams.includes(id)) {
      this.activeTeams = this.activeTeams.filter((teamId) => teamId !== id);
    } else {
      this.activeTeams.push(id);
    }
    this.changeTeamFilter();
  }
}
