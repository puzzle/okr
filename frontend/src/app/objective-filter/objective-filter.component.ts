import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { optionalReplaceWithNulls, sanitize } from '../shared/common';

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
    const sanitizedQuery = sanitize(this.form.value.objectiveControl!);
    const encoded = encodeURI(sanitizedQuery);
    const params = { objectiveQuery: encoded };
    const optionalParams = optionalReplaceWithNulls(params);
    this.router.navigate([], { queryParams: optionalParams }).then(() => this.refreshService.markDataRefresh());
  }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      const objectiveQuery = decodeURI(params['objectiveQuery'] || '');
      const sanitizedQuery = sanitize(objectiveQuery);
      if (sanitize(this.form.value.objectiveControl!) !== sanitizedQuery) {
        this.form.controls.objectiveControl.setValue(sanitizedQuery);
        this.updateURL();
      }
    });
  }
}
