import { Component, Input, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { getFullNameFromUser, User } from '../../shared/types/model/User';
import { Location } from '@angular/common';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CancelDialogComponent, CancelDialogData } from '../../shared/dialog/cancel-dialog/cancel-dialog.component';
import { OKR_DIALOG_CONFIG } from '../../shared/constantLibary';
import { filter, mergeMap } from 'rxjs';
import { AlertDialogComponent, AlertDialogData } from '../../shared/dialog/alert-dialog/alert-dialog.component';

@Component({
  selector: 'app-delete-user',
  templateUrl: './delete-user.component.html',
  styleUrl: './delete-user.component.scss',
})
export class DeleteUserComponent implements OnInit {
  @Input({ required: true }) user!: User;

  private userHasKeyResults: boolean = false;

  constructor(
    private readonly userService: UserService,
    private readonly location: Location,
    private readonly dialog: MatDialog,
  ) {}

  ngOnInit() {
    this.userService.isUserOwnerOfKeyResults(this.user).subscribe((booleanAsObject) => {
      this.userHasKeyResults = !!booleanAsObject;
    });
  }

  isUserMemberOfTeams(): boolean {
    return this.user.userTeamList != null && this.user.userTeamList.length > 0;
  }

  isUserOwnerOfKeyResults(): boolean {
    return this.userHasKeyResults;
  }

  deleteUser() {
    if (this.isUserMemberOfTeams()) {
      this.showUnableToDeleteDialog(this.dialogTitle(), this.dialogMessageUserIsInTeams());
      return;
    }
    if (this.isUserOwnerOfKeyResults()) {
      this.showUnableToDeleteDialog(this.dialogTitle(), this.dialogMessageUserHasKeyResults());
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

  private dialogMessageUserIsInTeams() {
    const userTeamsString = this.user.userTeamList.map((t) => `"${t.team.name}"`).join(', ');

    return `${this.userInfo()} ist in folgenden Teams und kann daher nicht gelöscht werden: ${userTeamsString}`;
  }

  private dialogMessageUserHasKeyResults() {
    return `${this.userInfo()} ist Owner folgender KeyResults und kann daher nicht gelöscht werden: TODO`;
  }

  showUnableToDeleteDialog(title: string, text: string) {
    const dialogConfig: MatDialogConfig<AlertDialogData> = OKR_DIALOG_CONFIG;
    dialogConfig.data = {
      dialogTitle: title,
      dialogText: text,
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
      .pipe(
        filter((confirm) => confirm),
        mergeMap(() => this.userService.deleteUser(user)),
      )
      .subscribe(() => {
        this.userService.reloadUsers();
        this.location.back();
      });
  }
}
