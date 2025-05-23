import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { getQueryString, optionalReplaceWithNulls, sanitize } from '../../shared/common';
import { debounceTime, map, Subject } from 'rxjs';

@Component({
  selector: 'app-objective-filter',
  templateUrl: './objective-filter.component.html',
  styleUrls: ['./objective-filter.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class ObjectiveFilterComponent implements OnInit {
  refresh = new Subject<void>();

  query = '';

  constructor(private router: Router,
    private route: ActivatedRoute) {
    this.refresh.pipe(debounceTime(300))
      .subscribe(() => this.updateUrl());
  }

  updateUrl() {
    const sanitizedQuery = sanitize(this.query);
    const params = { objectiveQuery: sanitizedQuery };
    const optionalParams = optionalReplaceWithNulls(params);
    this.router.navigate([], { queryParams: optionalParams });
  }

  ngOnInit() {
    this.route.queryParams.pipe(map((p) => p['objectiveQuery']))
      .subscribe((query) => {
        const objectiveQuery = getQueryString(query);
        if (sanitize(this.query) !== objectiveQuery) {
          this.query = objectiveQuery;
        }
      });
  }
}
