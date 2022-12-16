import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnInit,
} from '@angular/core';
import { MenuEntry } from '../../shared/types/menu-entry';
import { KeyResultMeasure } from '../../shared/services/key-result.service';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-keyresult-row',
  templateUrl: './key-result-row.component.html',
  styleUrls: ['./key-result-row.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyResultRowComponent implements OnInit {
  @Input() keyResult!: KeyResultMeasure;
  @Input() objectiveId!: number;
  menuEntries!: MenuEntry[];
  progressPercentage!: number;

  constructor(private datePipe: DatePipe, private router: Router) {}

  ngOnInit(): void {
    const elementMeasureValue =
      this.keyResult.measure?.value || this.keyResult.basicValue;
    const elementMeasureTargetValue = this.keyResult.targetValue;
    const elementMeasureBasicValue = this.keyResult.basicValue;
    this.calculateProgress(
      elementMeasureValue,
      elementMeasureTargetValue,
      elementMeasureBasicValue
    );
    this.menuEntries = [
      {
        displayName: 'KeyResult bearbeiten',
        routeLine:
          'objective/' +
          this.objectiveId +
          '/keyresult/edit/' +
          this.keyResult.id,
      },
      { displayName: 'KeyResult duplizieren', routeLine: 'objective/edit' },
      {
        displayName: 'Details einsehen',
        routeLine: 'keyresults/' + this.keyResult.id,
      },
      { displayName: 'KeyResult löschen', routeLine: 'result/add' },
      { displayName: 'Messung hinzufügen', routeLine: 'result/add' },
    ];
  }

  public formatDate(): string {
    const formattedDate = this.datePipe.transform(
      this.keyResult.measure?.createdOn,
      'dd.MM.yyyy'
    );
    if (formattedDate === null) {
      return '-';
    } else {
      return formattedDate;
    }
  }

  redirect(menuEntry: MenuEntry) {
    this.router.navigate([menuEntry.routeLine]);
  }

  private calculateProgress(
    elementMeasureValue: number,
    elementMeasureTargetValue: number,
    elementMeasureBasicValue: number
  ) {
    if (!this.keyResult.measure) {
      this.progressPercentage = 0;
    } else {
      this.progressPercentage = Math.abs(
        Math.round(
          (elementMeasureValue /
            (elementMeasureTargetValue - elementMeasureBasicValue)) *
            100
        )
      );
    }
  }
}
