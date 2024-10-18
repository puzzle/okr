import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { ComponentType } from '@angular/cdk/overlay';
import { isMobileDevice } from '../shared/common';
import { CONFIRM_DIALOG_WIDTH, OKR_DIALOG_CONFIG } from '../shared/constantLibary';

@Injectable({
  providedIn: 'root',
})
export class DialogService {
  DIALOG_CONFIG = OKR_DIALOG_CONFIG;

  constructor(private readonly dialog: MatDialog) {}

  open<T, D = any, R = any>(component: ComponentType<T>, config?: MatDialogConfig<D>): MatDialogRef<T, R> {
    return this.dialog.open(component, {
      ...this.DIALOG_CONFIG,
      data: config?.data,
    });
  }

  // openConfirmDialog(message: string, title: string = 'Confirm') {}
  //
  // openConfirmDialog(message: string, title: string = 'Confirm') {}
}
