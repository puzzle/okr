import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Team, TeamService } from '../shared/services/team.service';
import { Observable } from 'rxjs';
import { Quarter, QuarterService } from '../shared/services/quarter.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent implements OnInit {
  teams = new FormControl('');
  teamList!: Observable<Team[]>;
  quarters$!: Observable<Quarter[]> | undefined;

  constructor(
    private teamService: TeamService,
    private quarterService: QuarterService
  ) {}

  ngOnInit(): void {
    this.teamList = this.teamService.getTeams();
    this.quarters$ = this.quarterService.getQuarters();
  }

  changeTeamFilter(value: Team[]) {
    console.log(value);
  }

  changeQuarterFilter(value: Quarter) {
    console.log(value);
  }
}
