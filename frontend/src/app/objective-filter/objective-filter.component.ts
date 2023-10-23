import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-objective-filter',
  templateUrl: './objective-filter.component.html',
  styleUrls: ['./objective-filter.component.scss'],
})
export class ObjectiveFilterComponent implements OnInit {
  objectiveControl = new FormControl<string>('');

  constructor(
    private router: Router,
    private refreshService: RefreshDataService,
    private route: ActivatedRoute,
  ) {}

  updateURL() {
    this.router
      .navigate([], { queryParams: { objectiveQuery: this.objectiveControl.value } })
      .then(() => this.refreshService.markDataRefresh());
  }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      const objectiveQuery = (params['objectiveQuery'] as string) || '';
      if (this.objectiveControl.value?.toLowerCase().trim() !== objectiveQuery.toLowerCase().trim()) {
        console.log(objectiveQuery);
        this.objectiveControl.patchValue(objectiveQuery);
        console.log(this.objectiveControl.value);
        this.updateURL();
      }
    });
  }
}
