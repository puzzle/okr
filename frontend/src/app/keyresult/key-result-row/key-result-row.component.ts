import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnInit,
} from '@angular/core';
import { MenuEntry } from '../../shared/types/menu-entry';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-keyresult-row',
  templateUrl: './key-result-row.component.html',
  styleUrls: ['./key-result-row.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyResultRowComponent implements OnInit {
  @Input() keyResult!: KeyResultMeasure;
  @Input() menuEntries!: MenuEntry[];
  progressPercentage!: number;

  constructor(private datePipe: DatePipe) {}

  ngOnInit(): void {
    const elementMeasureValue =
      this.keyResult.measure?.value || this.keyResult.basicValue;
    const elementMeasureTargetValue = this.keyResult.targetValue;
    const elementMeasureBasicValue = this.keyResult.basicValue;
    this.progressPercentage = Math.round(
      (elementMeasureValue /
        (elementMeasureTargetValue - elementMeasureBasicValue)) *
        100
    );
  }

  public formatDate(): string {
    const formattedDate = this.datePipe.transform(
      this.keyResult.measure?.createdOn,
      'dd.MM.yyyy'
    );
    if (formattedDate == null) {
      return '-';
    } else {
      return formattedDate;
    }
  }
}
