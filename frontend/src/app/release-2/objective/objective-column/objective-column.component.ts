import { Component, OnInit } from '@angular/core';
import { OverviewService } from '../../shared/services/overview.service';
import { KeyResultMeasure } from '../../../release-1/shared/services/key-result.service';
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
        return '/assets/ongoing-status-indicator.svg';
      case 'DISSATISFIED':
        return '/assets/dissatisfied-status-indicator.svg';
      case 'SATISFIED':
        return '/assets/very-satisfied-status-indicator.svg';
      default:
        return '/assets/draft-status-indicator.svg';
    }
  }

  buttonDemo() {
    console.log('Button of objective-column works!');
  }

  protected readonly console = console;
}
