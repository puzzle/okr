import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompleteDialogComponent } from './complete-dialog.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

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

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompleteDialogComponent],
      providers: [
        { provide: MatDialogRef, useValue: dialogMock },
        { provide: MAT_DIALOG_DATA, useValue: matDataMock },
      ],
    });
    fixture = TestBed.createComponent(CompleteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set right classes on init', () => {
    let elements = document.querySelectorAll('.card-hover');
    let submitButton = document.querySelectorAll('button')[1];

    expect(elements.length).toEqual(2);
    expect(component.completeForm.value.isSuccessful).toBeNull();
    expect(component.completeForm.value.comment).toBeNull();
    expect(component.completeForm.invalid).toBeTruthy();
    expect(submitButton.disabled).toBeTruthy();
  });

  it('should change isSuccessful value on card click and remove class card-hover', () => {
    component.switchSuccessState('successful');
    let elements = document.querySelectorAll('.card-hover');
    let submitButton = document.querySelectorAll('button')[1];

    expect(component.completeForm.value.isSuccessful).toBeTruthy();
    expect(component.completeForm.invalid).toBeFalsy();
    expect(elements.length).toEqual(0);
    expect(submitButton.disabled).toBeTruthy();

    component.completeForm.patchValue({ isSuccessful: null });
    component.switchSuccessState('notSuccessful');
    elements = document.querySelectorAll('.card-hover');

    expect(component.completeForm.value.isSuccessful).toBeFalsy();
    expect(elements.length).toEqual(0);
  });

  it('should set active and non-active classes on switch', () => {
    component.switchSuccessState('successful');
    fixture.detectChanges();
    let nonActiveElement = document.querySelector('.non-active');

    expect(nonActiveElement!.innerHTML).toContain('Objective nicht erfolgreich');

    component.switchSuccessState('notSuccessful');
    fixture.detectChanges();
    nonActiveElement = document.querySelector('.non-active');

    expect(nonActiveElement!.innerHTML).toContain('Objective erfolgreich');
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
