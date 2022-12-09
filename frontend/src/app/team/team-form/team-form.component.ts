import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Team, TeamService } from '../team.service';
import { map, Observable } from 'rxjs';

@Component({
  selector: 'app-team-form',
  templateUrl: './team-form.component.html',
  styleUrls: ['./team-form.component.scss'],
})
export class TeamFormComponent implements OnInit {
  isCreating: boolean = true;
  teamObject!: Team;
  teamname!: string;

  constructor(
    private teamService: TeamService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.route.snapshot.params['id'] != null) {
      this.teamObject = window.history.state;
      this.teamObject.id = this.route.snapshot.params['id'];
      this.teamname = this.teamObject.name;
      this.isCreating = false;
    } else {
      this.isCreating = true;
    }
  }

  submit(teamName: string) {
    if (this.isCreating) {
      this.teamService
        .createTeam(Object(teamName)['teamName'])
        .subscribe((answer) => this.router.navigate(['/', 'teams']));
    } else {
      this.teamObject.name = Object(teamName)['teamName'];
      this.teamService
        .updateTeam(this.teamObject)
        .subscribe((answer) => this.router.navigate(['/', 'teams']));
    }
  }
}
