import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CheckInMin } from '../shared/types/model/CheckInMin';

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
  @Input() checkIn!: CheckInMin;

  ngOnChanges(changes: SimpleChanges) {
    if (!changes['checkIn']?.currentValue) {
      this.checkIn = { confidence: 5 } as CheckInMin;
    }
  }
}
