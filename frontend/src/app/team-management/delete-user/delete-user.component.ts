import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../shared/types/model/user';
import { Location } from '@angular/common';
import { Observable, Subject, takeUntil, tap } from 'rxjs';
import { UserTeam } from '../../shared/types/model/user-team';
import { ConfirmDialogData, DialogService } from '../../services/dialog.service';
import { ButtonState } from '../../shared/types/enums/button-state';
import { UserOkrData } from '../../shared/types/model/user-okr-data';

@Component({
  selector: 'app-delete-user',
  templateUrl: './delete-user.component.html',
  standalone: false
})
export class DeleteUserComponent implements OnInit, OnDestroy {
  @Input({ required: true }) user!: User;

  @Input({ required: true }) currentTeams$!: Observable<UserTeam[]>;

  okrUser: User | undefined;

  userOkrData: UserOkrData | undefined;

  userIsMemberOfTeams: boolean | undefined;

  unsubscribe$ = new Subject<void>();

  constructor(private readonly userService: UserService,
    private readonly dialogService: DialogService,
    private readonly location: Location) {}

  ngOnInit() {
    this.loadOkrUser();
    this.loadUserOkrData();
    this.updateUserTeamsStatusWhenTeamOfUserChanges();
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  private loadOkrUser() {
    this.userService
      .getOrInitCurrentUser()
      .pipe(takeUntil(this.unsubscribe$), tap((user) => this.okrUser = user))
      .subscribe();
  }

  loadUserOkrData() {
    this.userService
      .getUserOkrData(this.user)
      .pipe(takeUntil(this.unsubscribe$), tap((okrData) => this.userOkrData = okrData))
      .subscribe();
  }

  updateUserTeamsStatusWhenTeamOfUserChanges(): void {
    this.currentTeams$.subscribe(() => {
      this.loadUserMemberOfTeamsStatus();
    });
  }

  loadUserMemberOfTeamsStatus() {
    this.userService
      .isUserMemberOfTeams(this.user)
      .pipe(takeUntil(this.unsubscribe$), tap((isMemberOfTeams) => this.userIsMemberOfTeams = isMemberOfTeams))
      .subscribe();
  }

  hasOkrUserRoleOkrChampion() {
    if (this.okrUser == undefined) {
      return false;
    }
    return this.okrUser.isOkrChampion;
  }

  deleteUserWithChecks() {
    if (this.isUserMemberOfTeams()) {
      const dialogTitle = 'User kann nicht gelöscht werden';
      const dialogText = `${this.user.fullName} ist in folgenden Teams und kann daher nicht gelöscht werden: ${this.getDialogDetailsUserTeams()}`;
      this.showUnableToDeleteUserDialog(dialogTitle, dialogText);
      return;
    } else if (this.isUserOwnerOfKeyResults()) {
      const dialogTitle = 'User kann nicht gelöscht werden';
      const dialogText = `${this.user.fullName} ist Owner folgender KeyResults und kann daher nicht gelöscht werden: \n\n${this.dialogDetailsUserKeyResults()}`;
      this.showUnableToDeleteUserDialog(dialogTitle, dialogText);
      return;
    }
    this.deleteUser();
  }

  private isUserMemberOfTeams(): boolean {
    return this.userIsMemberOfTeams !== undefined ? this.userIsMemberOfTeams.valueOf() : true;
  }

  private isUserOwnerOfKeyResults(): boolean {
    return this.userOkrData !== undefined ? this.userOkrData.keyResults.length > 0 : true;
  }

  getDialogDetailsUserTeams() {
    if (this.userOkrData) {
      return this.user.userTeamList //
        .map((userTeam) => userTeam.team.name)
        .join(', ');
    }
    return '';
  }

  dialogDetailsUserKeyResults() {
    if (this.userOkrData) {
      return this.userOkrData.keyResults
        .map((data) => data.keyResultName + '\n(Objective: ' + data.objectiveName + ')')
        .join('\n\n');
    }
    return '';
  }

  showUnableToDeleteUserDialog(dialogTitle: string, dialogText: string) {
    const data: ConfirmDialogData = {
      title: dialogTitle,
      text: dialogText,
      yesButtonState: ButtonState.HIDDEN,
      noButtonState: ButtonState.HIDDEN,
      closeButtonState: ButtonState.VISIBLE_ENABLED
    };
    this.dialogService.openCustomizedConfirmDialog(data);
  }

  deleteUser() {
    const data: ConfirmDialogData = {
      title: 'User löschen',
      text: `Möchtest du den User ${this.user.firstName} ${this.user.lastName} wirklich löschen?`,
      yesButtonState: ButtonState.VISIBLE_ENABLED,
      noButtonState: ButtonState.VISIBLE_ENABLED,
      closeButtonState: ButtonState.HIDDEN
    };
    const dialog = this.dialogService.openCustomizedConfirmDialog(data);

    dialog.afterClosed()
      .subscribe((result) => {
        if (result) {
          this.userService
            .deleteUser(this.user)
            .pipe(takeUntil(this.unsubscribe$), tap({
              next: () => {
                this.userService.reloadUsers();
                this.location.back();
              },
              error: () => {
                throw Error(`unable to delete user ${this.user.fullName} (with id ${this.user.id})`);
              }
            }))
            .subscribe();
        }
      });
  }
}
