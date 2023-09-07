import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { MenuEntry } from '../shared/types/menu-entry';
import { RouteService } from '../shared/services/route.service';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { State } from '../shared/types/enums/State';
import { objective } from '../shared/testData';

@Component({
  selector: 'app-objective-column',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveComponent {
  @Input() objective!: ObjectiveMin;
  menuEntries: MenuEntry[] = [
    { displayName: 'Objective bearbeiten', showDialog: false },
    { displayName: 'Objective duplizieren', showDialog: false },
    { displayName: 'Objective abschliessen', showDialog: false },
    { displayName: 'Objective freigeben', showDialog: false },
  ];

  constructor(private routeService: RouteService) {}

  redirect(menuEntry: MenuEntry) {
    if (menuEntry.showDialog) {
      this.openDialog();
    } else {
      this.routeService.navigate(menuEntry.routeLine!);
    }
  }

  openDialog() {
    throw new Error(
      'This function should not have been called, since openDialog should be false, even though it appears to be true!'
    );
  }
}
