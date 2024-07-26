import { Component, Input, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../shared/types/model/User';
import { Location } from '@angular/common';

@Component({
  selector: 'app-delete-user',
  templateUrl: './delete-user.component.html',
  styleUrl: './delete-user.component.scss',
})
export class DeleteUserComponent implements OnInit {
  @Input({ required: true }) user!: User;

  private isUserMemberOfTeams: boolean = false;
  private isUserOwnerOfKeyResults: boolean = false;

  constructor(
    private readonly userService: UserService,
    private readonly location: Location,
  ) {}

  ngOnInit() {
    this.memberOfTeams(this.user.id);
    this.ownerOfKeyResults(this.user.id);
  }

  memberOfTeams(userId: number) {
    this.isUserMemberOfTeams = this.user.userTeamList != null && this.user.userTeamList.length > 0;
    console.log('### member_of_teams', userId, this.isUserMemberOfTeams);
  }

  ownerOfKeyResults(userId: number) {
    this.isUserMemberOfTeams = this.user.userTeamList.length > 0;
    this.userService.isUserOwnerOfKeyResults(this.user).subscribe((booleanAsObject) => {
      this.isUserOwnerOfKeyResults = !!booleanAsObject;
      console.log('### key_result', userId, !!booleanAsObject);
    });
  }

  deleteUser() {
    if (this.isUserMemberOfTeams) {
      alert('user is member of team');
      return;
    }
    if (this.isUserOwnerOfKeyResults) {
      alert('user is owner of key results');
      return;
    }

    this.userService.deleteUser(this.user).subscribe((v) => {
      this.location.back();
    });
  }
}
