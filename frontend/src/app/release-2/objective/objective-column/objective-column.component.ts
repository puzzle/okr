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
  }
}
