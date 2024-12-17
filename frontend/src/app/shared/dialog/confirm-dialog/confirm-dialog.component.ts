import { Component, Inject, OnInit } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { ConfirmDialogData } from "../../../services/dialog.service";
import { ButtonState } from "../../types/enums/ButtonState";

@Component({
  selector: "app-confirm-dialog",
  templateUrl: "./confirm-dialog.component.html",
  styleUrls: ["./confirm-dialog.component.scss"]
})
export class ConfirmDialogComponent implements OnInit {
  dialogTitle = "";

  dialogText = "";

  yesButtonState?: ButtonState;

  noButtonState?: ButtonState;

  closeButtonState?: ButtonState;

  constructor (@Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData,
    public dialogRef: MatDialogRef<ConfirmDialogComponent>) {}

  ngOnInit () {
    this.dialogTitle = this.data.title;
    this.dialogText = this.data.text;
    this.yesButtonState = this.data.yesButtonState ?? ButtonState.VisibleEnabled;
    this.noButtonState = this.data.noButtonState ?? ButtonState.VisibleEnabled;
    this.closeButtonState = this.data.closeButtonState ?? ButtonState.Hidden;
  }

  closeAndDelete () {
    this.dialogRef.close(true);
  }

  isYesButtonVisible () {
    return this.yesButtonState === ButtonState.VisibleEnabled || this.yesButtonState === ButtonState.VisibleDisabled;
  }

  isYesButtonDisabled () {
    return this.yesButtonState === ButtonState.VisibleDisabled;
  }

  isNoButtonVisible () {
    return this.noButtonState === ButtonState.VisibleEnabled || this.noButtonState === ButtonState.VisibleDisabled;
  }

  isNoButtonDisabled () {
    return this.noButtonState === ButtonState.VisibleDisabled;
  }

  isCloseButtonVisible () {
    return (
      this.closeButtonState === ButtonState.VisibleEnabled || this.closeButtonState === ButtonState.VisibleDisabled
    );
  }

  isCloseButtonDisabled () {
    return this.closeButtonState === ButtonState.VisibleDisabled;
  }
}
