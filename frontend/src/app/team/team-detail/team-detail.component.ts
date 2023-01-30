import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { Overview } from '../../shared/services/overview.service';

@Component({
  selector: 'app-team-detail',
  templateUrl: './team-detail.component.html',
  styleUrls: ['./team-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamDetailComponent {
  @Input() overview!: Overview;
  @Output() onObjectivesListUpdate: EventEmitter<any> = new EventEmitter();

  constructor() {}

  reloadObjectives() {
    this.onObjectivesListUpdate.emit();
  }
}
