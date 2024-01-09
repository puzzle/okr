import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/user.service';
import { BehaviorSubject, Subject, takeUntil, tap } from 'rxjs';
import { getFullNameFromUser, User } from '../../shared/types/model/User';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Team } from '../../shared/types/model/Team';
import { UserTeam } from '../../shared/types/model/UserTeam';
import { TranslateService } from '@ngx-translate/core';
import { MatTable } from '@angular/material/table';
import { TeamService } from '../../services/team.service';

@Component({
  selector: 'app-member-detail',
  templateUrl: './member-detail.component.html',
  styleUrl: './member-detail.component.scss',
})
export class MemberDetailComponent implements OnInit, OnDestroy {
  @ViewChild(MatTable) table!: MatTable<User[]>;

  user: User | undefined;
  teams: Team[] = [];
  currentUserTeams$ = new BehaviorSubject<UserTeam[]>([]);
  selectedUserIsLoggedInUser: boolean = false;
  unsubscribe$ = new Subject<void>();
  userTeamEditId: number | undefined;

  readonly displayedColumns = ['team', 'role', 'delete'];
  readonly getFullNameFromUser = getFullNameFromUser;

  constructor(
    private readonly userService: UserService,
    private readonly route: ActivatedRoute,
    private readonly translateService: TranslateService,
    private readonly teamService: TeamService,
    private readonly cd: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.route.paramMap
      .pipe(
        takeUntil(this.unsubscribe$),
        tap((params) => {
          const id = this.getIdFromParams(params);
          this.loadUser(id);
        }),
      )
      .subscribe();
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  private loadUser(userId: number) {
    this.userService
      .getUserById(userId)
      .pipe(tap((user) => this.setSelectedUserIsLoggedinUser(user)))
      .subscribe((user) => {
        this.user = user;
        this.currentUserTeams$.next(user.userTeamList);
        this.cd.markForCheck();
      });
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

  removeUserFromTeam(userTeam: UserTeam, user: User) {
    this.teamService
      .removeUserFromTeam(user.id, userTeam.team)
      .pipe(tap(() => this.loadUser(user.id)))
      .subscribe();
  }

  saveTeamRole(userTeam: UserTeam, user: User) {
    this.userTeamEditId = undefined;
    this.teamService
      .updateOrAddTeamMembership(user, userTeam)
      .pipe(
        tap(() => this.loadUser(user.id)),
        tap(() => this.userService.reloadUsers),
      )
      .subscribe();
  }

  isEditable(userTeam: UserTeam) {
    return userTeam.team.isWriteable;
  }

  isDeletable(userTeam: UserTeam, user: User): boolean {
    return this.isEditable(userTeam) || this.selectedUserIsLoggedInUser;
  }

  saveIsAdmin(userTeam: UserTeam, user: User, isAdmin: boolean) {
    userTeam.isTeamAdmin = isAdmin;
    this.saveTeamRole(userTeam, user);
  }
}
