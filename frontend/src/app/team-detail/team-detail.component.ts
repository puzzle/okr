import { Component, Input, OnInit } from '@angular/core';
import { Team } from '../dashboard/team.service';
import { Observable } from 'rxjs';
import { Objective, ObjectiveService } from './objective.service';

@Component({
  selector: 'app-team-detail',
  templateUrl: './team-detail.component.html',
  styleUrls: ['./team-detail.component.scss'],
})
export class TeamDetailComponent implements OnInit {
  @Input() teams!: Observable<Team[]>;

  constructor(public objectiveService: ObjectiveService) {}

  ngOnInit(): void {}
}
