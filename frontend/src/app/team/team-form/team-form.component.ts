import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Team, TeamService } from '../../shared/services/team.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Location } from '@angular/common';

@Component({
  selector: 'app-team-form',
  templateUrl: './team-form.component.html',
  styleUrls: ['./team-form.component.scss'],
})
export class TeamFormComponent implements OnInit {
  isCreating!: boolean;
  teamObject!: Team;
  teamForm!: FormGroup;

  constructor(
    private teamService: TeamService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    if (this.route.snapshot.params['id'] != null) {
      this.teamObject = window.history.state;
      this.teamObject.id = this.route.snapshot.params['id'];
      this.isCreating = false;
    } else {
      this.teamObject = this.teamService.getInitTeam();
      this.isCreating = true;
    }

    this.teamForm = new FormGroup({
      teamName: new FormControl<string>(this.teamObject.name, [
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(250),
      ]),
    });
  }

  save() {
    this.teamObject.name = Object(this.teamForm.value)['teamName'];
    this.teamService
      .save(this.teamObject)
      .subscribe((answer) => this.router.navigate(['/', 'teams']));
  }

  navigateBack() {
    this.location.back();
  }
}
