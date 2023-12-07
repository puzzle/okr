import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss'],
})
export class ConfirmDialogComponent implements OnInit {
  dialogTitle: string = '';
  dialogText: string = '';
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    private translate: TranslateService,
  ) {}

  ngOnInit() {
    if (this.data.draftCreate) {
      this.dialogTitle = 'Check-in im Draft-Status';
      this.dialogText =
        'Dein Objective befindet sich noch im DRAFT Status. Möchtest du das Check-in trotzdem erfassen?';
    } else if (this.data.action) {
      if (this.data.action === 'release') {
        this.dialogTitle = this.data.title + ' veröffentlichen';
        this.dialogText = 'Soll dieses ' + this.data.title + ' veröffentlicht werden?';
      }
    } else {
      this.dialogTitle = this.data.title + ' löschen';
      if (this.data.isAction) {
        this.dialogText = 'Möchtest du diese Action wirklich löschen?';
      } else {
        let error;
        switch (this.data.title) {
          case 'Team':
            error = 'DELETE_TEAM';
            break;
          case 'Objective':
            error = 'DELETE_OBJECTIVE';
            break;
          case 'Key Result':
            error = 'DELETE_KEY_RESULT';
            break;
        }
        this.dialogText = this.translate.instant('INFORMATION.' + error);
      }
    }
  }

  closeAndDelete() {
    this.dialogRef.close(true);
  }
}
