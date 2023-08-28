import { Component, OnInit } from '@angular/core';
import { OverviewService } from '../../shared/services/overview.service';
import { MenuEntry } from '../../shared/types/menu-entry';
import { RouteService } from '../../shared/services/route.service';
@Component({
  selector: 'app-objective-column',
  templateUrl: './objective-column.component.html',
  styleUrls: ['./objective-column.component.scss'],
})
export class ObjectiveColumnComponent implements OnInit {
  objectiveTitle: string = '';
  state: String = 'DRAFT';
  menuEntries!: MenuEntry[];

  constructor(private overviewService: OverviewService, private routeService: RouteService) {}

  ngOnInit(): void {
    this.setObjectiveAndKeyResultProperties();
  }

  setObjectiveAndKeyResultProperties() {
    const objectiveWithKeyresults = this.overviewService.getObjectiveWithKeyresults();
    this.objectiveTitle = objectiveWithKeyresults.title;
    this.state = objectiveWithKeyresults.state;
  }

  getCorrectStateSrc() {
    switch (this.state) {
      case 'ONGOING':
        return '/assets/icons/ongoing-icon.svg';
      case 'DISSATISFIED':
        return '/assets/icons/not-successful-icon.svg';
      case 'SATISFIED':
        return '/assets/icons/successful-icon.svg';
      default:
        return '/assets/icons/draft-icon.svg';
    }
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
