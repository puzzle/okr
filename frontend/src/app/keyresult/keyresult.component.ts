import { Component, Input } from '@angular/core';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';
import { MatDialog } from '@angular/material/dialog';
import { CheckInHistoryDialogComponent } from '../shared/dialog/check-in-history-dialog/check-in-history-dialog.component';
@Component({
  selector: 'app-keyresult',
  templateUrl: './keyresult.component.html',
  styleUrls: ['./keyresult.component.scss'],
})
export class KeyresultComponent {
  @Input() keyResult!: KeyresultMin;
  constructor(public dialog: MatDialog) {}
  checkInHistory() {
    const dialogRef = this.dialog.open(CheckInHistoryDialogComponent, {
      data: {
        keyResultId: this.keyResult.id,
      },
    });

    dialogRef.afterClosed().subscribe(() => {});
  }
}
