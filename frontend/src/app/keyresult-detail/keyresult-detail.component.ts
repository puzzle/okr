import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges } from '@angular/core';
import { KeyResult } from '../shared/types/model/KeyResult';
import { KeyresultService } from '../shared/services/keyresult.service';

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

  checkIfKeyresultIsMetric(keyresult: string) {
    return keyresult == 'metric';
  }

  ngOnChanges() {
    this.keyResultService.getFullKeyResult(this.keyResultId).subscribe((fullKeyResult) => {
      this.keyResult = fullKeyResult;
      this.changeDetectorRef.markForCheck();
    });
  }
}
