import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

@Component({
  selector: 'app-drawer-content',
  templateUrl: './drawer-content.component.html',
  styleUrls: ['./drawer-content.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DrawerContentComponent {
  @Input() drawerContent!: { id: number; type: string };
}
