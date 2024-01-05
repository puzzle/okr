import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/user.service';
import { mergeMap, Observable, tap } from 'rxjs';
import { getFullNameFromUser, User } from '../../shared/types/model/User';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Team } from '../../shared/types/model/Team';
import { UserTeam } from '../../shared/types/model/UserTeam';
import { TranslateService } from '@ngx-translate/core';
import { MatTable } from '@angular/material/table';

@Component({
  selector: 'app-member-detail',
  templateUrl: './member-detail.component.html',
  styleUrl: './member-detail.component.scss',
})
export class MemberDetailComponent implements OnInit {
  @ViewChild(MatTable) table!: MatTable<User[]>;

  user$: Observable<User> | undefined;
  teams: Team[] = [];
  selectedUserIsLoggedInUser: boolean = false;
  readonly displayedColumns = ['name', 'role', 'delete'];

  readonly getFullNameFromUser = getFullNameFromUser;

  constructor(
    private readonly userService: UserService,
    private readonly route: ActivatedRoute,
    private readonly translateService: TranslateService,
  ) {}
  ngOnInit(): void {
    this.user$ = this.route.paramMap.pipe(
      mergeMap((params) => {
        const id = this.getIdFromParams(params);
        return this.userService.getUserById(id);
      }),
      tap((user) => this.setSelectedUserIsLoggedinUser(user)),
    );
  }

  private setSelectedUserIsLoggedinUser(selectedUser: User) {
    this.selectedUserIsLoggedInUser = selectedUser.id === this.userService.getCurrentUser().id;
  }

  private getIdFromParams(params: ParamMap): number {
    const id = params.get('id');
    if (!id) {
      throw Error('member id is undefined');
    }
    return parseInt(id);
  }

  getRole(userTeam: UserTeam): string {
    if (userTeam.isTeamAdmin) {
      return this.translateService.instant('USER_ROLE.TEAM_ADMIN');
    }
    return this.translateService.instant('USER_ROLE.TEAM_MEMBER');
  }

  removeTeamMembership(userTeam: UserTeam, user: User) {
    alert('not implemented');
  }

  editTeamMembership(userTeam: UserTeam, user: User) {
    alert('not implemented');
  }

  isEditable(userTeam: UserTeam) {
    return userTeam.team.isWriteable;
  }

  isDeletable(userTeam: UserTeam, user: User): boolean {
    return this.isEditable(userTeam) || this.selectedUserIsLoggedInUser;
  }
}
