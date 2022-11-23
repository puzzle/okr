import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { OkrCycle, Team, TeamService } from './team.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  teams = new FormControl('');
  cycles = new FormControl('');
  teamList!: Observable<Team[]>;
  cycleList!: OkrCycle[] | undefined;

  constructor(private teamService: TeamService) {}

  ngOnInit(): void {
    this.teamList = this.teamService.getTeams();
    this.cycleList = this.teamService.getQuarter();
  }

  changeTeamFilter(value: Team[]) {
    console.log(value);
  }

  changeCycleFilter(value: OkrCycle) {
    console.log(value);
  }
}
