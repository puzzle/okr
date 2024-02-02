import { Component, ViewChild } from '@angular/core';
import { NewUser } from '../../shared/types/model/NewUser';
import { NgForm } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { DialogRef } from '@angular/cdk/dialog';

@Component({
  selector: 'app-invite-user-dialog',
  templateUrl: './invite-user-dialog.component.html',
  styleUrl: './invite-user-dialog.component.scss',
})
export class InviteUserDialogComponent {
  @ViewChild('form') form!: NgForm;

  private readonly emptyUser = { firstname: '', lastname: '', email: '' };

  users: NewUser[] = [{ ...this.emptyUser }];

  constructor(
    private readonly userService: UserService,
    private readonly dialogRef: DialogRef,
  ) {}

  inviteUsers() {
    this.userService.createUsers(this.users).subscribe(() => this.dialogRef.close());
  }

  addUser() {
    this.users.push({ ...this.emptyUser });
  }

  removeUser(user: NewUser) {
    this.users = this.users.filter((u) => u !== user);
  }
}
