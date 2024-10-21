import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { ComponentType } from '@angular/cdk/overlay';
import { isMobileDevice } from '../shared/common';
import { CONFIRM_DIALOG_WIDTH, OKR_DIALOG_CONFIG } from '../shared/constantLibary';
import { ConfirmDialogComponent } from '../shared/dialog/confirm-dialog/confirm-dialog.component';
import { TranslateService } from '@ngx-translate/core';

export interface ConfirmDialogData {
  title: string;
  text: string;
}

@Injectable({
  providedIn: 'root',
})
export class DialogService {
  DIALOG_CONFIG = OKR_DIALOG_CONFIG;

  constructor(
    private readonly dialog: MatDialog,
    private readonly translationService: TranslateService,
  ) {}

  open<T, D = any, R = any>(component: ComponentType<T>, config?: MatDialogConfig<D>): MatDialogRef<T, R> {
    return this.dialog.open(component, {
      ...this.DIALOG_CONFIG,
      data: config?.data,
    });
  }

  openConfirmDialog(translationKey: string) {
    const tile = this.translationService.instant(`${translationKey}.TITLE`);
    const text = this.translationService.instant(`${translationKey}.TEXT`);
    return this.openConfirmDialogWithCustomText(tile, text);
  }

  openConfirmDialogWithCustomText(title: string, text: string) {
    const dialogConfig = isMobileDevice()
      ? {
          maxWidth: '100vw',
          maxHeight: '100vh',
          height: '100vh',
          width: CONFIRM_DIALOG_WIDTH,
        }
      : {
          width: '45em',
          height: 'auto',
        };

    return this.dialog.open(ConfirmDialogComponent, {
      ...dialogConfig,
      data: {
        title: title,
        text: text,
      },
    });
  }
}
