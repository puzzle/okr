import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { MenuEntry } from '../shared/types/menu-entry';
import { RouteService } from '../shared/services/route.service';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';
import { NotifierService } from '../shared/services/notifier.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-objective-column',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveComponent {
  @Input() objective!: ObjectiveMin;
  @Input() objectiveMin!: ObjectiveMin;
  menuEntries: MenuEntry[] = [
    { displayName: 'Objective bearbeiten', showDialog: false },
    { displayName: 'Objective duplizieren', showDialog: false },
    { displayName: 'Objective abschliessen', showDialog: false },
    { displayName: 'Objective freigeben', showDialog: false },
  ];

  constructor(
    private routeService: RouteService,
    private notifierService: NotifierService,
    private router: Router,
  ) {}

  redirect(menuEntry: MenuEntry) {
    if (menuEntry.showDialog) {
      this.openDialog();
    } else {
      this.routeService.navigate(menuEntry.routeLine!);
    }
  }

  openDialog() {
    throw new Error(
      'This function should not have been called, since openDialog should be false, even though it appears to be true!',
    );
  }

  openObjectiveDetail() {
    this.router.navigate(['objective', this.objective.id]);
  }
}
