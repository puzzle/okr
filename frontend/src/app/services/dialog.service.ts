import { Injectable } from "@angular/core";
import { MatDialog, MatDialogConfig, MatDialogRef } from "@angular/material/dialog";
import { ComponentType } from "@angular/cdk/overlay";
import { ConfirmDialogComponent } from "../shared/dialog/confirm-dialog/confirm-dialog.component";
import { TranslateService } from "@ngx-translate/core";
import { ButtonState } from "../shared/types/enums/ButtonState";

export interface ConfirmDialogData {
  title: string;
  text: string;
  yesButtonState?: ButtonState;
  noButtonState?: ButtonState;
  closeButtonState?: ButtonState;
}

@Injectable({
  providedIn: "root"
})
export class DialogService {
  DIALOG_PANEL_CLASS_DEFAULT = "okr-dialog-panel-default";

  DIALOG_PANEL_CLASS_SMALL = "okr-dialog-panel-small";

  DIALOG_CONFIG: MatDialogConfig = {
    maxWidth: "100vw", // Used to override the default maxWidth of angular material dialog
    restoreFocus: true,
    autoFocus: "first-tabbable"
  };

  constructor(private readonly dialog: MatDialog,
    private readonly translationService: TranslateService) {}

  open<T, D = any, R = any>(component: ComponentType<T>, config?: MatDialogConfig<D>): MatDialogRef<T, R> {
    return this.dialog.open(component, {
      panelClass: this.DIALOG_PANEL_CLASS_DEFAULT,
      ...this.DIALOG_CONFIG,
      ...config
    });
  }

  openConfirmDialog(translationKey: string, i18nData?: object): MatDialogRef<ConfirmDialogComponent> {
    const title = this.translationService.instant(`${translationKey}.TITLE`, i18nData);
    const text = this.translationService.instant(`${translationKey}.TEXT`, i18nData);
    return this.open(ConfirmDialogComponent, {
      panelClass: this.DIALOG_PANEL_CLASS_SMALL,
      data: {
        title: title,
        text: text
      }
    });
  }

  openCustomizedConfirmDialog(data: ConfirmDialogData): MatDialogRef<ConfirmDialogComponent> {
    return this.open(ConfirmDialogComponent, {
      panelClass: this.DIALOG_PANEL_CLASS_SMALL,
      data: {
        title: data.title,
        text: data.text,
        yesButtonState: data.yesButtonState,
        noButtonState: data.noButtonState,
        closeButtonState: data.closeButtonState
      }
    });
  }
}
