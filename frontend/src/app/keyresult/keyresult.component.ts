import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';
import { MatDialog } from '@angular/material/dialog';
import { CheckInHistoryDialogComponent } from '../shared/dialog/check-in-history-dialog/check-in-history-dialog.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-keyresult',
  templateUrl: './keyresult.component.html',
  styleUrls: ['./keyresult.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KeyresultComponent {
  @Input() keyResult!: KeyresultMin;
  constructor(
    public dialog: MatDialog,
    private router: Router,
  ) {}
  checkInHistory() {
    const dialogRef = this.dialog.open(CheckInHistoryDialogComponent, {
      data: {
        keyResultId: this.keyResult.id,
      },
    });

    dialogRef.afterClosed().subscribe(() => {});
  }

  openDrawer() {
    this.router.navigate(['keyresult', this.keyResult.id]);
  }
}
