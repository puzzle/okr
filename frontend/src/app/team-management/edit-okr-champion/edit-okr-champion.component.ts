import { ChangeDetectorRef, Component, ElementRef, HostListener, Input } from '@angular/core';
import { User } from '../../shared/types/model/User';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-edit-okr-champion',
  templateUrl: './edit-okr-champion.component.html',
  styleUrl: './edit-okr-champion.component.scss',
})
export class EditOkrChampionComponent {
  @Input({ required: true }) user!: User;

  edit = false;

  constructor(
    private readonly userService: UserService,
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
    this.userService.setOkrChampion(this.user, okrChampion).subscribe(() => {
      this.user.isOkrChampion = okrChampion;
      this.setEditAsync(false);
      this.userService.reloadUsers();
      this.userService.reloadCurrentUser().subscribe();
      this.cd.markForCheck();
    });
  }
}
