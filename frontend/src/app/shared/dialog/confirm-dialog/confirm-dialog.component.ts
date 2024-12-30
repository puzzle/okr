import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogData } from '../../../services/dialog.service';
import { ButtonState } from '../../types/enums/button-state';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss'],
  standalone: false
})
export class ConfirmDialogComponent implements OnInit {
  dialogTitle = '';

  dialogText = '';

  yesButtonState?: ButtonState;

  noButtonState?: ButtonState;

  closeButtonState?: ButtonState;

  constructor(@Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData,
    public dialogRef: MatDialogRef<ConfirmDialogComponent>) {}

  ngOnInit() {
    this.dialogTitle = this.data.title;
    this.dialogText = this.data.text;
    this.yesButtonState = this.data.yesButtonState ?? ButtonState.VISIBLE_ENABLED;
    this.noButtonState = this.data.noButtonState ?? ButtonState.VISIBLE_ENABLED;
    this.closeButtonState = this.data.closeButtonState ?? ButtonState.HIDDEN;
  }

  closeAndDelete() {
    this.dialogRef.close(true);
  }

  isYesButtonVisible() {
    return this.yesButtonState === ButtonState.VISIBLE_ENABLED || this.yesButtonState === ButtonState.VISIBLE_DISABLED;
  }

  isYesButtonDisabled() {
    return this.yesButtonState === ButtonState.VISIBLE_DISABLED;
  }

  isNoButtonVisible() {
    return this.noButtonState === ButtonState.VISIBLE_ENABLED || this.noButtonState === ButtonState.VISIBLE_DISABLED;
  }

  isNoButtonDisabled() {
    return this.noButtonState === ButtonState.VISIBLE_DISABLED;
  }

  isCloseButtonVisible() {
    return (
      this.closeButtonState === ButtonState.VISIBLE_ENABLED || this.closeButtonState === ButtonState.VISIBLE_DISABLED
    );
  }

  isCloseButtonDisabled() {
    return this.closeButtonState === ButtonState.VISIBLE_DISABLED;
  }
}
