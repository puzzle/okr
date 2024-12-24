import { ChangeDetectorRef, Component, ElementRef, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { UserTeam } from '../../shared/types/model/user-team';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-show-edit-role',
  templateUrl: './show-edit-role.component.html',
  styleUrl: './show-edit-role.component.scss'
})
export class ShowEditRoleComponent {
  @Input({ required: true }) userTeam!: UserTeam;

  @Output()
  private readonly save = new EventEmitter<boolean>();

  edit = false;

  constructor(private readonly translate: TranslateService,
    private readonly elementRef: ElementRef,
    private readonly cd: ChangeDetectorRef) {}

  @HostListener('document:click', ['$event'])
  clickOutside(event: MouseEvent) {
    if (this.elementRef.nativeElement.contains(event.target)) {
      return;
    }
    this.edit = false;
  }

  /*
   * we set edit async, to ensure hostListener can detect outside-of-element clicks correctly
   * otherwise element of event.target is already hidden
   */
  setEditAsync($event: MouseEvent, edit: boolean) {
    $event.stopPropagation();
    setTimeout(() => {
      this.edit = edit;
      this.cd.markForCheck();
    }, 0);
  }

  saveIsAdmin(isAdmin: boolean) {
    this.edit = false;
    this.save.emit(isAdmin);
  }

  getRole(): string {
    if (this.userTeam.isTeamAdmin) {
      return this.translate.instant('USER_ROLE.TEAM_ADMIN');
    }
    return this.translate.instant('USER_ROLE.TEAM_MEMBER');
  }

  isEditable() {
    return this.userTeam.team.isWriteable;
  }
}
