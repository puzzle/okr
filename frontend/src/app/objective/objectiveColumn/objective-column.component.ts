import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { OverviewService } from '../../shared/services/overview.service';
import { MenuEntry } from '../../shared/types/menu-entry';
import { RouteService } from '../../shared/services/route.service';
import { Observable, Subject } from 'rxjs';
import { Objective } from '../../shared/models/Objective';

@Component({
  selector: 'app-objective-column',
  templateUrl: './objective-column.component.html',
  styleUrls: ['./objective-column.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveColumnComponent implements OnInit {
  objective: Observable<Objective> = new Subject();
  menuEntries: MenuEntry[] = [
    { displayName: 'Objective bearbeiten', showDialog: false },
    { displayName: 'Objective duplizieren', showDialog: false },
    { displayName: 'Objective abschliessen', showDialog: false },
    { displayName: 'Objective freigeben', showDialog: false },
  ];

  constructor(private overviewService: OverviewService, private routeService: RouteService) {}

  ngOnInit(): void {
    this.objective = this.overviewService.getObjectiveWithKeyresults();
  }

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
