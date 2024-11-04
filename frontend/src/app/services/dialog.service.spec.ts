import { TestBed } from '@angular/core/testing';

import { DialogService } from './dialog.service';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../shared/dialog/confirm-dialog/confirm-dialog.component';
import { AddEditTeamDialog } from '../team-management/add-edit-team-dialog/add-edit-team-dialog.component';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

describe('DialogService', () => {
  let service: DialogService;
  let matDialogSpy: MatDialog;
  let translateServiceSpy: TranslateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [provideHttpClient(), provideHttpClientTesting(), TranslateService],
    });
    matDialogSpy = TestBed.inject(MatDialog);
    service = TestBed.inject(DialogService);
    translateServiceSpy = TestBed.inject(TranslateService);
    jest.spyOn(matDialogSpy, 'open');
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should open dialog', () => {
    const dialog = service.open(AddEditTeamDialog, {
      data: {
        aProperty: 'aValue',
      },
    });
    expect(dialog).toBeInstanceOf(MatDialogRef);
    expect(dialog.componentInstance).toBeInstanceOf(AddEditTeamDialog);

    expect(matDialogSpy.open).toHaveBeenCalledWith(AddEditTeamDialog, {
      panelClass: service.DIALOG_PANEL_CLASS_DEFAULT,
      ...service.DIALOG_CONFIG,
      data: {
        aProperty: 'aValue',
      },
    });
  });

  it('should open confirm dialog', () => {
    const i18nData = {
      team: 'the a-team',
    };
    jest.spyOn(service, 'open');
    jest.spyOn(translateServiceSpy, 'instant');
    const dialog = service.openConfirmDialog('DELETE.TEAM', i18nData);

    //Call function of own service
    expect(service.open).toHaveBeenCalledTimes(1);
    expect(dialog).toBeInstanceOf(MatDialogRef);
    expect(translateServiceSpy.instant).toHaveBeenCalledTimes(2);
    expect(translateServiceSpy.instant).toHaveBeenCalledWith('DELETE.TEAM.TITLE', i18nData);
    expect(translateServiceSpy.instant).toHaveBeenCalledWith('DELETE.TEAM.TEXT', i18nData);

    //Call function of angular material dialog
    expect(matDialogSpy.open).toHaveBeenCalledTimes(1);
    expect(dialog.componentInstance).toBeInstanceOf(ConfirmDialogComponent);
    expect(dialog.componentInstance.data.title).toBe('DELETE.TEAM.TITLE');
    expect(dialog.componentInstance.data.text).toBe('DELETE.TEAM.TEXT');

    expect(matDialogSpy.open).toHaveBeenCalledWith(ConfirmDialogComponent, {
      panelClass: service.DIALOG_PANEL_CLASS_SMALL,
      ...service.DIALOG_CONFIG,
      data: {
        title: 'DELETE.TEAM.TITLE',
        text: 'DELETE.TEAM.TEXT',
      },
    });
  });
});
