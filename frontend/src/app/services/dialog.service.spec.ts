import { TestBed } from '@angular/core/testing';

import { ConfirmDialogData, DialogService } from './dialog.service';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../shared/dialog/confirm-dialog/confirm-dialog.component';
import { AddEditTeamDialog } from '../team-management/add-edit-team-dialog/add-edit-team-dialog.component';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { ButtonState } from '../shared/types/enums/ButtonState';

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

  function expectData(current: ConfirmDialogData, expected: ConfirmDialogData) {
    expect(current.title).toBe(expected.title);
    expect(current.text).toBe(expected.text);
    expect(current.yesButtonState).toBe(expected.yesButtonState);
    expect(current.noButtonState).toBe(expected.noButtonState);
    expect(current.closeButtonState).toBe(expected.closeButtonState);
  }

  function expectYesButtonIsVisibleAndEnabled(dialog: ConfirmDialogComponent) {
    expect(dialog.isYesButtonVisible()).toBe(true);
    expect(dialog.isYesButtonDisabled()).toBe(false);
  }

  function expectNoButtonIsVisibleAndEnabled(dialog: ConfirmDialogComponent) {
    expect(dialog.isNoButtonVisible()).toBe(true);
    expect(dialog.isNoButtonDisabled()).toBe(false);
  }

  function expectCloseButtonIsHiddenAndEnabled(dialog: ConfirmDialogComponent) {
    expect(dialog.isCloseButtonVisible()).toBe(false);
    expect(dialog.isCloseButtonDisabled()).toBe(false);
  }

  function expectNoButtonIsVisibleAndDisabled(dialog: ConfirmDialogComponent) {
    expect(dialog.isNoButtonVisible()).toBe(true);
    expect(dialog.isNoButtonDisabled()).toBe(true);
  }

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

  it('should open customized confirm dialog with default visibility properties', () => {
    // arrange
    const data: ConfirmDialogData = {
      title: 'Test title',
      text: 'Test description',
      yesButtonState: undefined,
      noButtonState: undefined,
      closeButtonState: undefined,
    };

    jest.spyOn(service, 'open');
    jest.spyOn(translateServiceSpy, 'instant');

    // act
    const dialog = service.openCustomizedConfirmDialog(data);
    dialog.componentInstance.ngOnInit(); // trigger ngOnInit() manually in the test

    // assert
    expect(service.open).toHaveBeenCalledTimes(1);
    expect(dialog).toBeInstanceOf(MatDialogRef);
    expect(matDialogSpy.open).toHaveBeenCalledTimes(1);

    let confirmDialogInstance = dialog.componentInstance;
    expect(confirmDialogInstance).toBeInstanceOf(ConfirmDialogComponent);
    expectData(confirmDialogInstance.data, data);

    expect(matDialogSpy.open).toHaveBeenCalledWith(ConfirmDialogComponent, {
      panelClass: service.DIALOG_PANEL_CLASS_SMALL,
      ...service.DIALOG_CONFIG,
      data,
    });

    expectYesButtonIsVisibleAndEnabled(confirmDialogInstance);
    expectNoButtonIsVisibleAndEnabled(confirmDialogInstance);
    expectCloseButtonIsHiddenAndEnabled(confirmDialogInstance);
  });

  it('should open customized confirm dialog with explicit visibility properties', () => {
    // arrange
    const data: ConfirmDialogData = {
      title: 'Test title',
      text: 'Test description',
      yesButtonState: ButtonState.Visible_Enabled,
      noButtonState: ButtonState.Visible_Disabled,
      closeButtonState: ButtonState.Hidden,
    };

    jest.spyOn(service, 'open');
    jest.spyOn(translateServiceSpy, 'instant');

    // act
    const dialog = service.openCustomizedConfirmDialog(data);
    dialog.componentInstance.ngOnInit(); // trigger ngOnInit() manually in the test

    // assert
    expect(service.open).toHaveBeenCalledTimes(1);
    expect(dialog).toBeInstanceOf(MatDialogRef);
    expect(matDialogSpy.open).toHaveBeenCalledTimes(1);

    let confirmDialogInstance = dialog.componentInstance;
    expect(confirmDialogInstance).toBeInstanceOf(ConfirmDialogComponent);
    expectData(confirmDialogInstance.data, data);

    expect(matDialogSpy.open).toHaveBeenCalledWith(ConfirmDialogComponent, {
      panelClass: service.DIALOG_PANEL_CLASS_SMALL,
      ...service.DIALOG_CONFIG,
      data,
    });

    expectYesButtonIsVisibleAndEnabled(confirmDialogInstance);
    expectNoButtonIsVisibleAndDisabled(confirmDialogInstance);
    expectCloseButtonIsHiddenAndEnabled(confirmDialogInstance);
  });
});
