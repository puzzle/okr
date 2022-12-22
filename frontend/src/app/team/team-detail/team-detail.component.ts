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
import { Overview } from '../../shared/services/overview.service';

@Component({
  selector: 'app-team-detail',
  templateUrl: './team-detail.component.html',
  styleUrls: ['./team-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamDetailComponent {
  @Input() overview!: Overview;

  constructor() {}
}
