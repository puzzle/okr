import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompleteDialogComponent } from './complete-dialog.component';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { DialogTemplateCoreComponent } from '../../custom/dialog-template-core/dialog-template-core.component';
import { MatDividerModule } from '@angular/material/divider';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

const dialogMock = {
  close: jest.fn(),
};

let matDataMock: { objective: { objectiveId: number | undefined; teamId: number | undefined } } = {
  objective: {
    objectiveId: undefined,
    teamId: 1,
  },
};

describe('CompleteDialogComponent', () => {
  let component: CompleteDialogComponent;
  let fixture: ComponentFixture<CompleteDialogComponent>;
  let debugElement: any;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, ReactiveFormsModule, MatDialogModule, MatIconModule, MatDividerModule],
      declarations: [CompleteDialogComponent, DialogTemplateCoreComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MatDialogRef, useValue: dialogMock },
        { provide: MAT_DIALOG_DATA, useValue: matDataMock },
        { provide: TranslateService, useValue: {} },
      ],
    });
    fixture = TestBed.createComponent(CompleteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    debugElement = fixture.debugElement.nativeElement;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set right classes on init', () => {
    let elements = document.querySelectorAll('.valuation-card');
    let successful = document.querySelectorAll('.card-hover-successful');
    let notSuccessful = document.querySelectorAll('.card-hover-not-successful');
    let submitButton = debugElement.querySelector('[data-testid="submit"]');

    expect(elements.length).toEqual(2);
    expect(successful.length).toEqual(1);
    expect(notSuccessful.length).toEqual(1);
    expect(component.completeForm.value.isSuccessful).toBeNull();
    expect(component.completeForm.value.comment).toBeNull();
    expect(component.completeForm.invalid).toBeTruthy();
    expect(submitButton!.hasAttribute('disabled')).toBeTruthy();
  });

  it('should change isSuccessful value on card click and remove class card-hover', () => {
    component.switchSuccessState('successful');
    let elements = document.querySelectorAll('.card-hover');
    let submitButton = debugElement.querySelector('[data-testid="submit"]');

    expect(component.completeForm.value.isSuccessful).toBeTruthy();
    expect(component.completeForm.invalid).toBeFalsy();
    expect(elements.length).toEqual(0);
    expect(submitButton!.hasAttribute('disabled')).toBeTruthy();

    component.completeForm.patchValue({ isSuccessful: null });
    component.switchSuccessState('notSuccessful');
    elements = document.querySelectorAll('.card-hover');

    expect(component.completeForm.value.isSuccessful).toBeFalsy();
    expect(elements.length).toEqual(0);
  });

  it('should set active and non-active classes on switch', () => {
    component.switchSuccessState('successful');
    fixture.detectChanges();
    let nonActiveElement = document.querySelector('.active-successful');

    expect(nonActiveElement!.innerHTML).toContain('Objective erreicht');

    component.switchSuccessState('notSuccessful');
    fixture.detectChanges();
    nonActiveElement = document.querySelector('.active-not-successful');

    expect(nonActiveElement!.innerHTML).toContain('Objective nicht erreicht');
  });

  it('should close dialog with right data', () => {
    component.completeForm.patchValue({
      isSuccessful: true,
      comment: 'My new comment',
    });
    component.closeDialog();

    expect(dialogMock.close).toHaveBeenCalledTimes(1);
    expect(dialogMock.close).toHaveBeenCalledWith({
      endState: 'SUCCESSFUL',
      comment: 'My new comment',
    });
  });
});
