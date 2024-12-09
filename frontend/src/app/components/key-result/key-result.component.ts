import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { KeyResultMin } from '../../shared/types/model/key-result-min';
import { Router } from '@angular/router';
import { DATE_FORMAT } from '../../shared/constant-library';
import { KeyResultMetricMin } from '../../shared/types/model/key-result-metric-min';
import { KeyResultOrdinalMin } from '../../shared/types/model/key-result-ordinal-min';

@Component({
  selector: 'app-key-result',
  templateUrl: './key-result.component.html',
  styleUrls: ['./key-result.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class KeyResultComponent {
  @Input() keyResult!: KeyResultMin;

  protected readonly DATE_FORMAT = DATE_FORMAT;

  constructor(private router: Router) {}

  openDrawer() {
    this.router.navigate(['details/keyresult',
      this.keyResult.id]);
  }

  getKeyResultWithCorrectType(): KeyResultOrdinalMin | KeyResultMetricMin {
    if (this.keyResult.keyResultType === 'metric') {
      return this.keyResult as KeyResultMetricMin;
    } else {
      return this.keyResult as KeyResultOrdinalMin;
    }
  }
}
