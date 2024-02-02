import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InviteUserDialogComponent } from './invite-user-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NewUserComponent } from '../new-user/new-user.component';
import { PuzzleIconComponent } from '../../shared/custom/puzzle-icon/puzzle-icon.component';
import { PuzzleIconButtonComponent } from '../../shared/custom/puzzle-icon-button/puzzle-icon-button.component';

describe('InviteUserDialogComponent', () => {
  let component: InviteUserDialogComponent;
  let fixture: ComponentFixture<InviteUserDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InviteUserDialogComponent, NewUserComponent, PuzzleIconComponent, PuzzleIconButtonComponent],
      imports: [MatDialogModule, FormsModule, ReactiveFormsModule],
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
});
