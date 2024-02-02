import { Component, ViewChild } from "@angular/core";
import { NewUser } from "../../shared/types/model/NewUser";
import { NgForm } from "@angular/forms";

@Component({
  selector: 'app-invite-user-dialog',
  templateUrl: './invite-user-dialog.component.html',
  styleUrl: './invite-user-dialog.component.scss'
})
export class InviteUserDialogComponent {

  @ViewChild('form') form!: NgForm;

  private readonly emptyUser = {firstname: '', lastname: '', email: ''};

  users: NewUser[] = [{...this.emptyUser}];

  constructor() {
  }

  inviteUsers() {
  }

  addUser() {
    this.users.push({...this.emptyUser});
  }

  removeUser(user: NewUser) {
    this.users = this.users.filter(u => u !== user);
  }
}
