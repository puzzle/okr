import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnInit,
} from '@angular/core';
import { Team } from '../../shared/services/team.service';
import { Observable } from 'rxjs';
import {
  Objective,
  ObjectiveService,
} from '../../shared/services/objective.service';

@Component({
  selector: 'app-team-detail',
  templateUrl: './team-detail.component.html',
  styleUrls: ['./team-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamDetailComponent implements OnInit {
  @Input() team!: Team;
  objectiveList!: Observable<Objective[]>;

  constructor(public objectiveService: ObjectiveService) {}

  ngOnInit(): void {
    this.objectiveList = this.objectiveService.getObjectivesOfTeam(
      this.team.id
    );
  }
}
