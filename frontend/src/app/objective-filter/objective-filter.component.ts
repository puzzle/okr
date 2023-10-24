import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { optionalReplaceWithNulls, sanitize } from '../shared/common';
import { debounceTime, Subject, tap } from 'rxjs';

@Component({
  selector: 'app-objective-filter',
  templateUrl: './objective-filter.component.html',
  styleUrls: ['./objective-filter.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveFilterComponent implements OnInit {
  refresh: Subject<void> = new Subject();
  query: string = '';

  constructor(
    private router: Router,
    private refreshService: RefreshDataService,
    private route: ActivatedRoute,
  ) {
    this.refresh.pipe(debounceTime(300)).subscribe(() => this.updateURL());
  }

  updateURL() {
    const sanitizedQuery = sanitize(this.query);
    const encoded = encodeURI(sanitizedQuery);
    const params = { objectiveQuery: encoded };
    const optionalParams = optionalReplaceWithNulls(params);
    this.router.navigate([], { queryParams: optionalParams }).then(() => this.refreshService.markDataRefresh());
  }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      const objectiveQuery = decodeURI(params['objectiveQuery'] || '');
      const sanitizedQuery = sanitize(objectiveQuery);
      if (sanitize(this.query) !== sanitizedQuery) {
        this.query = sanitizedQuery;
        this.updateURL();
      }
    });
  }
}
