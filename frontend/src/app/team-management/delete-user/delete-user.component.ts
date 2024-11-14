import { Component, Input, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../shared/types/model/User';
import { Location } from '@angular/common';
import { Observable, Subject, takeUntil, tap } from 'rxjs';
import { UserTeam } from '../../shared/types/model/UserTeam';
import { ConfirmDialogData, DialogService } from '../../services/dialog.service';
import { ButtonState } from '../../shared/types/enums/ButtonState';

@Component({
  selector: 'app-delete-user',
  templateUrl: './delete-user.component.html',
  styleUrl: './delete-user.component.scss',
})
export class DeleteUserComponent implements OnInit {
  @Input({ required: true }) user!: User;
  @Input({ required: true }) currentTeams$!: Observable<UserTeam[]>;

  okrUser: User | undefined;
  unsubscribe$ = new Subject<void>();

  constructor(
    private readonly userService: UserService,
    private readonly dialogService: DialogService,
    private readonly location: Location,
  ) {}

  ngOnInit() {
    this.loadOkrUser();
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  private loadOkrUser() {
    this.userService
      .getOrInitCurrentUser()
      .pipe(
        takeUntil(this.unsubscribe$),
        tap((user) => (this.okrUser = user)),
      )
      .subscribe();
  }

  public hasOkrUserRoleOkrChampion() {
    if (this.okrUser == undefined) return false;
    return this.okrUser.isOkrChampion;
  }

  deleteUser() {
    const data: ConfirmDialogData = {
      title: `User löschen`,
      text: `Möchtest du den User ${this.user.firstname} ${this.user.lastname} wirklich löschen?`,
      yesButtonState: ButtonState.Visible_Enabled,
      noButtonState: ButtonState.Visible_Enabled,
      closeButtonState: ButtonState.Hidden,
    };
    const dialog = this.dialogService.openCustomizedConfirmDialog(data);

    dialog.afterClosed().subscribe((result) => {
      if (result) {
        this.userService.deleteUser(this.user).subscribe({
          next: () => {
            this.userService.reloadUsers();
            this.location.back();
          },
          error: () => {
            throw Error(`unable to delete user ${this.user.firstname} ${this.user.lastname} (with id ${this.user.id})`);
          },
        });
      }
    });
  }
}
