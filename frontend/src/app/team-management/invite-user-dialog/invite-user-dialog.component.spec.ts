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

describe('InviteUserDialogComponent', () => {
  let component: InviteUserDialogComponent;
  let fixture: ComponentFixture<InviteUserDialogComponent>;

  const userServiceMock = {
    createUsers: jest.fn(),
  };

  const dialogRefMock = {
    close: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InviteUserDialogComponent, NewUserComponent, PuzzleIconComponent, PuzzleIconButtonComponent],
      imports: [MatDialogModule, FormsModule, ReactiveFormsModule],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: DialogRef, useValue: dialogRefMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(InviteUserDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('addUser should add a user to the existing users', () => {
    component.addUser();
    expect(component.users.length).toBe(2);
  });

  it('removeUser should remove given user from users array', () => {
    const user1 = { firstname: 'user1', lastname: '1user', email: 'user1@user.ch' };
    const user2 = { firstname: 'user2', lastname: '2user', email: 'user2@user.ch' };
    const user3 = { firstname: 'user3', lastname: '3user', email: 'user3@user.ch' };
    component.users = [user1, user2, user3];

    component.removeUser(user2);

    expect(component.users).toStrictEqual([user1, user3]);
  });

  it('inviteUsers should call createUsers and close dialog', fakeAsync(() => {
    userServiceMock.createUsers.mockReturnValue(of([testUser]));
    component.inviteUsers();
    tick();

    expect(userServiceMock.createUsers).toBeCalledTimes(1);
    expect(userServiceMock.createUsers).toBeCalledWith(component.users);
    expect(dialogRefMock.close).toBeCalledTimes(1);
  }));
});
