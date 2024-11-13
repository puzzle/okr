import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogData } from '../../../services/dialog.service';
import { ButtonState } from '../../types/enums/ButtonState';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss'],
})
export class ConfirmDialogComponent implements OnInit {
  dialogTitle: string = '';
  dialogText: string = '';
  yesButtonState?: ButtonState;
  noButtonState?: ButtonState;
  closeButtonState?: ButtonState;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData,
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
  ) {}

  ngOnInit() {
    this.dialogTitle = this.data.title;
    this.dialogText = this.data.text;
    this.yesButtonState = this.data.yesButtonState ?? ButtonState.Visible_Enabled;
    this.noButtonState = this.data.noButtonState ?? ButtonState.Visible_Enabled;
    this.closeButtonState = this.data.closeButtonState ?? ButtonState.Hidden;
  }

  closeAndDelete() {
    this.dialogRef.close(true);
  }

  isYesButtonVisible() {
    return this.yesButtonState === ButtonState.Visible_Enabled || this.yesButtonState === ButtonState.Visible_Disabled;
  }

  isYesButtonDisabled() {
    return this.yesButtonState === ButtonState.Visible_Disabled;
  }

  isNoButtonVisible() {
    return this.noButtonState === ButtonState.Visible_Enabled || this.noButtonState === ButtonState.Visible_Disabled;
  }

  isNoButtonDisabled() {
    return this.noButtonState === ButtonState.Visible_Disabled;
  }

  isCloseButtonVisible() {
    return (
      this.closeButtonState === ButtonState.Visible_Enabled || this.closeButtonState === ButtonState.Visible_Disabled
    );
  }

  isCloseButtonDisabled() {
    return this.closeButtonState === ButtonState.Visible_Disabled;
  }
}
