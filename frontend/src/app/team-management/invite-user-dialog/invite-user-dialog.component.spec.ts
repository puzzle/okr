import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { InviteUserDialogComponent } from './invite-user-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NewUserComponent } from '../new-user/new-user.component';
import { PuzzleIconComponent } from '../../shared/custom/puzzle-icon/puzzle-icon.component';
import { PuzzleIconButtonComponent } from '../../shared/custom/puzzle-icon-button/puzzle-icon-button.component';
import { UserService } from '../../services/user.service';
import { testUser } from '../../shared/testData';
import { DialogRef } from '@angular/cdk/dialog';
import { of } from 'rxjs';
import { UniqueEmailValidator } from '../new-user/unique-mail.validator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('InviteUserDialogComponent', () => {
  let component: InviteUserDialogComponent;
  let fixture: ComponentFixture<InviteUserDialogComponent>;

  const user1 = { firstname: 'user1',
    lastname: '1user',
    email: 'user1@user.ch' };
  const user2 = { firstname: 'user2',
    lastname: '2user',
    email: 'user2@user.ch' };
  const user3 = { firstname: 'user3',
    lastname: '3user',
    email: 'user3@user.ch' };

  const userServiceMock = {
    createUsers: jest.fn(),
    getUsers: jest.fn()
  };

  const dialogRefMock = {
    close: jest.fn()
  };

  const uniqueMailValidatorMock = {
    setAddedMails: jest.fn(),
    validate: () => null
  };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [
        InviteUserDialogComponent,
        NewUserComponent,
        PuzzleIconComponent,
        PuzzleIconButtonComponent
      ],
      imports: [
        MatDialogModule,
        FormsModule,
        ReactiveFormsModule,
        MatFormFieldModule
      ],
      providers: [{ provide: UserService,
        useValue: userServiceMock },
      { provide: DialogRef,
        useValue: dialogRefMock },
      { provide: UniqueEmailValidator,
        useValue: uniqueMailValidatorMock }],
      schemas: [NO_ERRORS_SCHEMA]
    })
      .compileComponents();

    fixture = TestBed.createComponent(InviteUserDialogComponent);
    component = fixture.componentInstance;

    userServiceMock.createUsers.mockReset();
    dialogRefMock.close.mockReset();

    userServiceMock.getUsers.mockReturnValue(of([]));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('addUser should add a user to the existing users', () => {
    component.addUser();
    expect(component.form.controls.length)
      .toBe(2);
  });

  it('removeUser should remove given user from users array', () => {
    component.addUser();
    component.addUser();
    component.addUser();

    component.form.controls[0].setValue(user1);
    component.form.controls[1].setValue(user2);
    component.form.controls[2].setValue(user3);

    component.removeUser(1);

    expect(component.form.controls[0].value)
      .toStrictEqual(user1);
    expect(component.form.controls[1].value)
      .toStrictEqual(user3);
  });

  it('registerUsers should call createUsers and close dialog if form is valid', fakeAsync(() => {
    userServiceMock.createUsers.mockReturnValue(of([testUser]));

    component.form.controls[0].setValue(user1);

    component.registerUsers();
    tick();

    expect(userServiceMock.createUsers)
      .toBeCalledTimes(1);
    expect(userServiceMock.createUsers)
      .toBeCalledWith(component.form.value);
    expect(dialogRefMock.close)
      .toBeCalledTimes(1);
  }));

  it('registerUsers should not call createUsers form is not valid', fakeAsync(() => {
    component.registerUsers();
    tick();

    expect(userServiceMock.createUsers)
      .toBeCalledTimes(0);
    expect(dialogRefMock.close)
      .toBeCalledTimes(0);
  }));
});
