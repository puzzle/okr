import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { SidenavModel } from '../shared/types/model/SidenavModel';

@Component({
  selector: 'app-drawer-content',
  templateUrl: './drawer-content.component.html',
  styleUrls: ['./drawer-content.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DrawerContentComponent implements OnInit {
  @Input() drawerContent!: SidenavModel;

  constructor() {}

  ngOnInit(): void {}
}
