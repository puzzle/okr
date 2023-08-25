import { Component, OnInit } from '@angular/core';
import { OverviewService } from '../../shared/services/overview.service';
@Component({
  selector: 'app-objective-column',
  templateUrl: './objective-column.component.html',
  styleUrls: ['./objective-column.component.scss'],
})
export class ObjectiveColumnComponent implements OnInit {
  objectiveTitle: string = '';
  keyResults: String[] = []; //ToDo in KeyResult ticket
  state: String = 'DRAFT';

  constructor(private overviewService: OverviewService) {}

  ngOnInit(): void {
    this.setObjectiveAndKeyResultProperties();
  }

  setObjectiveAndKeyResultProperties() {
    const objectiveWithKeyresults = this.overviewService.getObjectiveWithKeyresults();
    this.objectiveTitle = objectiveWithKeyresults.title;

    //Add JSON string since there is no keyResult model yet
    for (let keyResult of objectiveWithKeyresults.keyresults) {
      this.keyResults.push(JSON.stringify(keyResult));
    }

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

  buttonDemo() {
    console.log('Button of objective-column works!');
  }
}
