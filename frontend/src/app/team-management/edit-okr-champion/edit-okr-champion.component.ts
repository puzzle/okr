import { ChangeDetectorRef, Component, ElementRef, EventEmitter, HostListener, Input, Output, inject } from '@angular/core';
import { User } from '../../shared/types/model/user';
import { UserService } from '../../services/user.service';
import { TeamService } from '../../services/team.service';

@Component({
  selector: 'app-edit-okr-champion',
  templateUrl: './edit-okr-champion.component.html',
  styleUrl: './edit-okr-champion.component.scss',
  standalone: false
})
export class EditOkrChampionComponent {
  private readonly userService = inject(UserService);

  private readonly teamService = inject(TeamService);

  private readonly cd = inject(ChangeDetectorRef);

  private elementRef = inject(ElementRef);

  @Input({ required: true }) user!: User;

  @Output() public okrChampionChange = new EventEmitter<boolean>();

  edit = false;

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
