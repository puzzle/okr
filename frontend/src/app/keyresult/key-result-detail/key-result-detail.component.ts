import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { KeyResultMeasure } from '../../shared/services/key-result.service';

@Component({
  selector: 'app-key-result-detail',
  templateUrl: './key-result-detail.component.html',
  styleUrls: ['./key-result-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyResultDetailComponent {
  @Input() keyResult!: KeyResultMeasure;
}
