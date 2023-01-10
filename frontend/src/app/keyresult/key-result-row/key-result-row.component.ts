import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { MenuEntry } from '../../shared/types/menu-entry';
import {
  KeyResultMeasure,
  KeyResultService,
} from '../../shared/services/key-result.service';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../shared/dialog/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-keyresult-row',
  templateUrl: './key-result-row.component.html',
  styleUrls: ['./key-result-row.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyResultRowComponent implements OnInit {
  @Input() keyResult!: KeyResultMeasure;
  @Input() objectiveId!: number;
  @Output() onKeyresultListUpdate: EventEmitter<any> = new EventEmitter();
  menuEntries!: MenuEntry[];
  progressPercentage!: number;

  constructor(
    private datePipe: DatePipe,
    private router: Router,
    private keyResultService: KeyResultService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    const elementMeasureValue =
      this.keyResult.measure != null ? this.keyResult.measure?.value : 0;
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
      { displayName: 'KeyResult löschen', routeLine: 'result/delete' },
      {
        displayName: 'Messung hinzufügen',
        routeLine: 'keyresults/' + this.keyResult.id + '/measure/new',
      },
    ];
  }

  public formatDate(): string {
    if (this.keyResult.measure?.measureDate == undefined) {
      return '-';
    }
    var convertedDate: Date = new Date(this.keyResult.measure?.measureDate!);
    const formattedDate = this.datePipe.transform(convertedDate, 'dd.MM.yyyy');
    if (formattedDate === null) {
      return '-';
    } else {
      return formattedDate;
    }
  }

  redirect(menuEntry: MenuEntry) {
    if (menuEntry.routeLine == 'result/delete') {
      this.openDialog();
    } else {
      this.router.navigate([menuEntry.routeLine]);
    }
  }

  openDialog() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title:
          'Willst du dieses Keyresult und die dazugehörigen Messungen wirklich löschen?',
        confirmText: 'Bestätigen',
        closeText: 'Abbrechen',
      },
    });

    dialogRef.afterClosed().subscribe((data) => {
      if (data) {
        this.keyResultService
          .deleteKeyResultById(this.keyResult.id!)
          .subscribe((data) => {
            this.onKeyresultListUpdate.emit(this.keyResult.objectiveId!);
          });
      }
    });
  }

  private calculateProgress(
    elementMeasureValue: number,
    elementMeasureTargetValue: number,
    elementMeasureBasicValue: number
  ) {
    if (elementMeasureValue === 0) {
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
