import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { NotifierService } from '../shared/services/notifier.service';

@Component({
  selector: 'app-drawer-content',
  templateUrl: './drawer-content.component.html',
  styleUrls: ['./drawer-content.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DrawerContentComponent {
  @Input() drawerContent!: { id: number; type: string };

  constructor(private notifierService: NotifierService) {}

  closeDrawer() {
    this.notifierService.closeDetailSubject.next();
  }
}
