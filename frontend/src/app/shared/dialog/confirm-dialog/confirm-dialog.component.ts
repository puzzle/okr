import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

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
  ) {}

  ngOnInit() {
    if (this.data.draftCreate) {
      this.dialogTitle = 'Check-in im Draft-Status';
      this.dialogText =
        'Möchtest du wirklich ein Check-in zu einem Key Result erstellen, wessen Objective im Status DRAFT ist?';
    } else {
      if (this.data.action) {
        if (this.data.action === 'release') {
          this.dialogTitle = this.data.title + ' veröffentlichen';
          this.dialogText = 'Soll dieses ' + this.data.title + ' veröffentlicht werden?';
        }
      } else {
        this.dialogTitle = this.data.title + ' löschen';
        this.dialogText = this.data.isAction
          ? 'Möchtest du diese Action wirklich löschen?'
          : 'Möchtest du dieses ' +
            this.data.title +
            ' wirklich löschen? Zugehörige ' +
            (this.data.title == 'Objective' ? 'Key Results' : 'Check-ins') +
            ' werden dadurch ebenfalls gelöscht!';
      }
    }
  }

  closeAndDelete() {
    this.dialogRef.close(true);
  }
}
