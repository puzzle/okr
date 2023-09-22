import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamComponent {
  @Input() overviewEntity!: OverviewEntity;
}
