import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { SidenavModel } from '../shared/types/model/SidenavModel';
import { ObjectiveService } from '../shared/services/objective.service';
import { Objective } from '../shared/types/model/Objective';

@Component({
  selector: 'app-drawer-content',
  templateUrl: './drawer-content.component.html',
  styleUrls: ['./drawer-content.component.scss'],
})
export class DrawerContentComponent implements OnInit, OnChanges {
  @Input() drawerContent!: SidenavModel;
  fullObjective!: Objective;
  constructor(private objectiveService: ObjectiveService) {}

  ngOnInit(): void {}

  ngOnChanges(changes: SimpleChanges) {
    if (changes['drawerContent'].currentValue != null) {
      this.objectiveService.getFullObjective(this.drawerContent.id).subscribe((fullObjective: Objective) => {
        this.fullObjective = fullObjective;
      });
    }
  }
}
