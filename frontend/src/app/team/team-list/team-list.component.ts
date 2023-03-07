import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Team, TeamService } from '../../shared/services/team.service';
import { RouteService } from '../../shared/services/route.service';

@Component({
  selector: 'app-team-list',
  templateUrl: './team-list.component.html',
  styleUrls: ['./team-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamListComponent implements OnInit {
  teamList$!: Observable<Team[]>;

  constructor(private teamService: TeamService, public routeService: RouteService) {}

  ngOnInit(): void {
    this.teamList$ = this.teamService.getTeams();
  }
}
