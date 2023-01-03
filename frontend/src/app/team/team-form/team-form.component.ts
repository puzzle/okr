import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Team, TeamService } from '../../shared/services/team.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { HttpStatusCode } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-team-form',
  templateUrl: './team-form.component.html',
  styleUrls: ['./team-form.component.scss'],
})
export class TeamFormComponent implements OnInit {
  teamObject: Observable<Team> = of(this.teamService.getInitTeam());
  teamForm: FormGroup = new FormGroup({
    name: new FormControl<string>('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(250),
    ]),
  });
  id?: number;

  constructor(
    private teamService: TeamService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    if (this.id) {
      this.teamObject = this.teamService.getTeam(this.id);
    }
  }

  save() {
    let saveTeam = {
      ...this.teamService.getInitTeam(),
      id: this.id,
      ...this.teamForm.value,
    } as Team;
    this.teamService.save(saveTeam).subscribe({
      next: () => {
        this.router.navigate(['/', 'teams']);
        this.toastr.success('Everything worked fine', 'Team created!', {
          timeOut: 5000,
        });
      },
      error: (value: HttpStatusCode) => {
        this.toastr.error("Can't save Team!", 'Error: ' + value, {
          timeOut: 5000,
        });
        return;
      },
    });
  }

  navigateBack() {
    this.location.back();
  }
}
