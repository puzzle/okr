import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UserTeam } from '../../shared/types/model/UserTeam';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-show-edit-role',
  templateUrl: './show-edit-role.component.html',
  styleUrl: './show-edit-role.component.scss',
})
export class ShowEditRoleComponent {
  @Input({ required: true }) userTeam!: UserTeam;

  @Output()
  private readonly save = new EventEmitter<UserTeam>();

  edit = false;

  constructor(private readonly translate: TranslateService) {}

  saveIsAdmin(isAdmin: boolean) {
    this.edit = false;
    this.userTeam.isTeamAdmin = isAdmin;
    this.save.emit(this.userTeam);
  }

  getRole(): string {
    if (this.userTeam.isTeamAdmin) {
      return this.translate.instant('USER_ROLE.TEAM_ADMIN');
    }
    return this.translate.instant('USER_ROLE.TEAM_MEMBER');
  }

  isEditable() {
    return this.userTeam.team.writeable;
  }

  setEdit($event: MouseEvent, edit: boolean) {
    $event.stopPropagation();
    this.edit = edit;
  }
}
