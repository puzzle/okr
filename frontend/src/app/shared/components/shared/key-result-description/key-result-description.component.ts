import { Component, Input } from '@angular/core';
import { Goal } from '../../../services/goal.service';

@Component({
  selector: 'app-key-result-description',
  templateUrl: './key-result-description.component.html',
  styleUrls: ['./key-result-description.component.scss'],
})
export class KeyResultDescriptionComponent {
  @Input() goal!: Goal;
}
