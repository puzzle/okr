import { Component, Input, OnInit } from '@angular/core';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';

@Component({
  selector: 'app-drawer-content',
  templateUrl: './drawer-content.component.html',
  styleUrls: ['./drawer-content.component.scss'],
})
export class DrawerContentComponent implements OnInit {
  @Input() drawerContent!: ObjectiveMin | KeyresultMin;
  constructor() {}

  ngOnInit(): void {}
}
