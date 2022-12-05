import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { MenuEntry } from '../../shared/types/menu-entry';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-keyresult-row',
  templateUrl: './key-result-row.component.html',
  styleUrls: ['./key-result-row.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class KeyResultRowComponent implements OnInit {
  @Input() keyResult!: KeyResultMeasure;
  @Input() menuEntries!: MenuEntry[];
  progressPercentage!: number;
  progressRecord!: Record<string, boolean>;

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
    this.progressRecord = {
      'progress-bar-bad': this.progressPercentage < 40,
      'progress-bar-medium': this.progressPercentage < 70,
      'progress-bar-good': this.progressPercentage <= 100,
    };
  }

  public formatDate(): string {
    const formattedDate = this.datePipe.transform(
      this.keyResult.measure?.createdOn,
      'dd.mM.Y'
    );
    if (formattedDate == null) {
      return '-';
    } else {
      return formattedDate;
    }
  }
}
