import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-objective-filter',
  templateUrl: './objective-filter.component.html',
  styleUrls: ['./objective-filter.component.scss'],
})
export class ObjectiveFilterComponent implements OnInit {
  form = new FormGroup({
    objectiveControl: new FormControl<string>(''),
  });

  constructor(
    private router: Router,
    private refreshService: RefreshDataService,
    private route: ActivatedRoute,
  ) {}

  updateURL() {
    this.router
      .navigate([], { queryParams: { objectiveQuery: this.form.value.objectiveControl } })
      .then(() => this.refreshService.markDataRefresh());
  }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      const objectiveQuery = (params['objectiveQuery'] as string) || '';
      // if (this.form.value.objectiveControl?.toLowerCase().trim() !== objectiveQuery.toLowerCase().trim()) {
      this.form.controls.objectiveControl.setValue(objectiveQuery);
      // this.updateURL();
      // }
    });
  }
}
