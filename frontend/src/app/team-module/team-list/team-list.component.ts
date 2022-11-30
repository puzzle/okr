import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Team, TeamService } from '../team.service';
import { NavigationExtras, Router } from '@angular/router';

@Component({
  selector: 'app-team-list',
  templateUrl: './team-list.component.html',
  styleUrls: ['./team-list.component.scss'],
})
export class TeamListComponent implements OnInit {
  teamList!: Observable<Team[]>;

  constructor(private teamService: TeamService, private router: Router) {}

  ngOnInit(): void {
    this.teamList = this.teamService.getTeams();
  }

  redirectToEdit(team: Team) {
    let url = '/team/edit/' + team.id;
    this.router.navigate([url, { team: team }]);
  }
}
