import { Component, Input, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { getFullNameFromUser, User } from '../../shared/types/model/User';
import { Location } from '@angular/common';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CancelDialogComponent, CancelDialogData } from '../../shared/dialog/cancel-dialog/cancel-dialog.component';
import { OKR_DIALOG_CONFIG } from '../../shared/constantLibary';
import { mergeMap, Observable } from 'rxjs';
import { AlertDialogComponent, AlertDialogData } from '../../shared/dialog/alert-dialog/alert-dialog.component';
import { UserOkrData } from '../../shared/types/model/UserOkrData';
import { UserTeam } from '../../shared/types/model/UserTeam';

@Component({
  selector: 'app-delete-user',
  templateUrl: './delete-user.component.html',
  styleUrl: './delete-user.component.scss',
})
export class DeleteUserComponent implements OnInit {
  @Input({ required: true }) user!: User;
  @Input({ required: true }) currentTeams$!: Observable<UserTeam[]>;

  userIsMemberOfTeams: Boolean | undefined;
  userOkrData: UserOkrData | undefined;

  constructor(
    private readonly userService: UserService,
    private readonly location: Location,
    private readonly dialog: MatDialog,
  ) {}

  ngOnInit() {
    this.currentTeams$.subscribe(() => {
      this.updateUserMemberTeamsStatus();
    });

    this.loadUserOkrData();
  }

  updateUserMemberTeamsStatus() {
    this.userService
      .isUserMemberOfTeams(this.user) //
      .subscribe((isMemberOfTeams) => (this.userIsMemberOfTeams = isMemberOfTeams));
  }

  loadUserOkrData() {
    this.userService //
      .getUserOkrData(this.user) //
      .subscribe((okrData) => (this.userOkrData = okrData));
  }

  isUserMemberOfTeams(): boolean {
    return this.userIsMemberOfTeams !== undefined ? this.userIsMemberOfTeams.valueOf() : true;
  }

  isUserOwnerOfKeyResults(): boolean {
    return this.userOkrData !== undefined ? this.userOkrData.keyResults.length > 0 : true;
  }

  deleteUser() {
    if (this.isUserMemberOfTeams()) {
      this.showUnableToDeleteDialog(
        this.dialogTitle(), //
        this.dialogTextUserIsInTeams(), //
        this.dialogDetailsUserTeams(8),
      );
      return;
    }
    if (this.isUserOwnerOfKeyResults()) {
      this.showUnableToDeleteDialog(
        this.dialogTitle(),
        this.dialogTextUserIsOwnerOfKeyResults(),
        this.dialogDetailsUserKeyResults(8),
      );
      return;
    }
    this.showDeleteUserDialog(this.user);
  }

  private dialogTitle() {
    return `${this.userInfo()} kann nicht gelöscht werden`;
  }

  private userInfo() {
    return getFullNameFromUser(this.user);
  }

  private dialogTextUserIsInTeams() {
    return `${this.userInfo()} ist in folgenden Teams und kann daher nicht gelöscht werden:`;
  }

  private dialogTextUserIsOwnerOfKeyResults() {
    return `${this.userInfo()} ist Owner folgender KeyResults und kann daher nicht gelöscht werden:`;
  }

  private dialogDetailsUserTeams(showMaxTeams: number) {
    if (this.userOkrData) {
      return this.user.userTeamList //
        .filter((_, index) => index < showMaxTeams) //
        .map((userTeam) => userTeam.team.name);
    }
    return [];
  }

  private dialogDetailsUserKeyResults(showMaxKeyResults: number) {
    if (this.userOkrData) {
      return this.userOkrData.keyResults
        .filter((_, index) => index < showMaxKeyResults)
        .map((data) => data.keyResultName + ' (Objective: ' + data.objectiveName + ')');
    }
    return [];
  }

  showUnableToDeleteDialog(title: string, text: string, details: string[]) {
    const dialogConfig: MatDialogConfig<AlertDialogData> = OKR_DIALOG_CONFIG;
    dialogConfig.data = {
      dialogTitle: title,
      dialogText: text,
      dialogDetails: details,
    };
    this.dialog.open(AlertDialogComponent, dialogConfig);
  }

  showDeleteUserDialog(user: User) {
    const dialogConfig: MatDialogConfig<CancelDialogData> = OKR_DIALOG_CONFIG;
    dialogConfig.data = {
      dialogTitle: `Member ${getFullNameFromUser(user)} wirklich löschen?`,
    };
    this.dialog
      .open(CancelDialogComponent, dialogConfig)
      .afterClosed()
      .pipe(mergeMap(() => this.userService.deleteUser(user)))
      .subscribe(() => {
        this.userService.reloadUsers();
        this.location.back();
      });
  }
}