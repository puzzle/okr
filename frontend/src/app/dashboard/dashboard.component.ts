import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { OkrQuarter, Team, TeamService } from '../shared/services/team.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  teams = new FormControl('');
  teamList!: Observable<Team[]>;
  quarterList!: OkrQuarter[] | undefined;

  constructor(private teamService: TeamService) {}

  ngOnInit(): void {
    this.teamList = this.teamService.getTeams();
    this.quarterList = this.teamService.getQuarter();
  }

  changeTeamFilter(value: Team[]) {
    console.log(value);
  }

  changeQuarterFilter(value: OkrQuarter) {
    console.log(value);
  }
}
