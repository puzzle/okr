import { TestBed } from '@angular/core/testing';

import { DialogService } from './dialog.service';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../shared/dialog/confirm-dialog/confirm-dialog.component';
import { TeamComponent } from '../components/team/team.component';

describe('DialogService', () => {
  let service: DialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [TranslateModule.forRoot()], providers: [TranslateService] });
    service = TestBed.inject(DialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should open dialog', () => {
    const dialog = service.open(TeamComponent);
    expect(dialog).toBeInstanceOf(MatDialogRef);
    expect(dialog._containerInstance._config.panelClass).toEqual(service.DIALOG_CONFIG.panelClass);
    expect(dialog._containerInstance._config.maxWidth).toEqual(service.DIALOG_CONFIG.maxWidth);
    expect(dialog.componentInstance).toBeInstanceOf(TeamComponent);
  });

  it('should open confirm dialog', () => {
    jest.spyOn(service, 'open');
    const dialog = service.openConfirmDialog('DELETE.ACTION');
    expect(service.open).toHaveBeenCalledTimes(1);
    expect(dialog).toBeInstanceOf(MatDialogRef);
    expect(dialog.componentInstance).toBeInstanceOf(ConfirmDialogComponent);
    expect(dialog.componentInstance.data.title).toBe('DELETE.ACTION.TITLE');
    expect(dialog.componentInstance.data.text).toBe('DELETE.ACTION.TEXT');
  });
});
