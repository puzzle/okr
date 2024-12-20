import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { KeyresultMin } from '../../shared/types/model/KeyresultMin';
import { Router } from '@angular/router';
import { DATE_FORMAT } from '../../shared/constantLibary';
import { KeyResultMetricMin } from '../../shared/types/model/KeyResultMetricMin';
import { KeyResultOrdinalMin } from '../../shared/types/model/KeyResultOrdinalMin';

@Component({
  selector: 'app-keyresult',
  templateUrl: './keyresult.component.html',
  styleUrls: ['./keyresult.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class KeyresultComponent {
  @Input() keyResult!: KeyresultMin;

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
