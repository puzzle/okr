import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/user.service';
import { BehaviorSubject, filter, mergeMap, Subject, takeUntil, tap } from 'rxjs';
import { getFullNameFromUser, User } from '../../shared/types/model/User';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Team } from '../../shared/types/model/Team';
import { UserTeam } from '../../shared/types/model/UserTeam';
import { TranslateService } from '@ngx-translate/core';
import { MatTable } from '@angular/material/table';
import { TeamService } from '../../services/team.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CancelDialogComponent, CancelDialogData } from '../../shared/dialog/cancel-dialog/cancel-dialog.component';
import { OKR_DIALOG_CONFIG } from '../../shared/constantLibary';

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
    private readonly router: Router,
    private readonly dialog: MatDialog,
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

  removeUserFromTeam(userTeam: UserTeam, user: User) {
    const dialogConfig: MatDialogConfig<CancelDialogData> = OKR_DIALOG_CONFIG;
    dialogConfig.data = {
      dialogTitle: getFullNameFromUser(user) + ` wirklich aus Team ${userTeam.team.name} entfernen?`,
    };
    this.dialog
      .open(CancelDialogComponent, dialogConfig)
      .afterClosed()
      .pipe(
        filter((confirm) => confirm),
        mergeMap(() => this.teamService.removeUserFromTeam(user.id, userTeam.team)),
      )
      .subscribe(() => this.loadUser(user.id));
  }

  updateTeamRole(isAdmin: boolean, userTeam: UserTeam, user: User) {
    this.userTeamEditId = undefined;
    // make a copy and set value of real object after successful request
    const newUserTeam = { ...userTeam };
    newUserTeam.isTeamAdmin = isAdmin;
    this.teamService.updateOrAddTeamMembership(user.id, newUserTeam).subscribe(() => {
      userTeam.isTeamAdmin = isAdmin;
      this.loadUser(user.id);
      this.userService.reloadUsers();
      this.userService.reloadCurrentUser().subscribe();
    });
  }

  addTeamRole(userTeam: UserTeam, user: User) {
    this.userTeamEditId = undefined;
    this.teamService.updateOrAddTeamMembership(user.id, userTeam).subscribe(() => {
      this.loadUser(user.id);
      this.userService.reloadUsers();
      this.userService.reloadCurrentUser().subscribe();
    });
  }

  isDeletable(userTeam: UserTeam): boolean {
    return userTeam.team.writeable || this.selectedUserIsLoggedInUser;
  }

  navigateBack() {
    this.router.navigate(['../'], { relativeTo: this.route.parent });
  }

  okrChampionChange(okrChampion: boolean, user: User) {
    this.userService.setOkrChampion(user, okrChampion).subscribe(() => {
      this.loadUser(user.id);
      this.userService.reloadUsers();
      this.teamService.reloadTeams();
      this.userService.reloadCurrentUser().subscribe();
    });
  }
}
