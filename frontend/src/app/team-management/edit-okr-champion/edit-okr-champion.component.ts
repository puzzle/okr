import { ChangeDetectorRef, Component, ElementRef, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { User } from '../../shared/types/model/User';
import { UserService } from '../../services/user.service';
import { TeamService } from '../../services/team.service';

@Component({
  selector: 'app-edit-okr-champion',
  templateUrl: './edit-okr-champion.component.html',
  styleUrl: './edit-okr-champion.component.scss',
})
export class EditOkrChampionComponent {
  @Input({ required: true }) user!: User;
  @Output() public okrChampionChange = new EventEmitter<boolean>();

  edit = false;

  constructor(
    private readonly userService: UserService,
    private readonly teamService: TeamService,
    private readonly cd: ChangeDetectorRef,
    private elementRef: ElementRef,
  ) {}

  @HostListener('document:click', ['$event'])
  clickOutside(event: MouseEvent) {
    if (this.elementRef.nativeElement.contains(event.target)) {
      return;
    }
    this.edit = false;
  }

  // we set edit async, to ensure hostListener can detect outside-of-element clicks correctly
  // otherwise element of event.target is already hidden
  setEditAsync(edit: boolean) {
    setTimeout(() => {
      this.edit = edit;
      this.cd.markForCheck();
    }, 0);
  }

  okrChampionEditable(): boolean {
    return this.userService.getCurrentUser().isOkrChampion;
  }

  setOkrChampion(okrChampion: boolean) {
    this.okrChampionChange.emit(okrChampion);
    this.setEditAsync(false);
    this.cd.markForCheck();
  }
}
