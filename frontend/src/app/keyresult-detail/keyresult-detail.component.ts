import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges } from '@angular/core';
import { KeyResult } from '../shared/types/model/KeyResult';
import { KeyresultService } from '../shared/services/keyresult.service';
import { KeyResultMetric } from '../shared/types/model/KeyResultMetric';
import { KeyResultOrdinal } from '../shared/types/model/KeyResultOrdinal';

@Component({
  selector: 'app-keyresult-detail',
  templateUrl: './keyresult-detail.component.html',
  styleUrls: ['./keyresult-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyresultDetailComponent implements OnChanges {
  @Input() keyResultId!: number;
  keyResult!: KeyResult;

  constructor(
    private keyResultService: KeyresultService,
    private changeDetectorRef: ChangeDetectorRef,
  ) {}

  ngOnChanges() {
    this.keyResultService.getFullKeyResult(this.keyResultId).subscribe((fullKeyResult) => {
      this.keyResult = fullKeyResult;
      this.changeDetectorRef.markForCheck();
    });
  }

  castToMetric(keyResult: KeyResult) {
    return keyResult as KeyResultMetric;
  }
  castToOrdinal(keyResult: KeyResult) {
    return keyResult as KeyResultOrdinal;
  }
}
