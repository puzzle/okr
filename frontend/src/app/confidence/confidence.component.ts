import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';
import { CheckInMin } from '../shared/types/model/CheckInMin';
import { KeyResult } from '../shared/types/model/KeyResult';

@Component({
  selector: 'app-confidence',
  templateUrl: './confidence.component.html',
  styleUrls: ['./confidence.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ConfidenceComponent implements OnChanges {
  min: number = 1;
  max: number = 10;
  @Input() edit: boolean = true;
  @Input() keyResult!: KeyresultMin | KeyResult;
  @Input() backgroundColor!: string;

  ngOnChanges(changes: SimpleChanges) {
    if (changes['keyResult']?.currentValue?.lastCheckIn === null) {
      this.keyResult.lastCheckIn = { confidence: 5 } as CheckInMin;
    }
  }
}
