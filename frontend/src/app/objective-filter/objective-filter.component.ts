import { AfterViewInit, ChangeDetectionStrategy, Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { getQueryString, optionalReplaceWithNulls, sanitize } from '../shared/common';
import { debounceTime, Subject } from 'rxjs';

@Component({
  selector: 'app-objective-filter',
  templateUrl: './objective-filter.component.html',
  styleUrls: ['./objective-filter.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ObjectiveFilterComponent implements AfterViewInit {
  refresh: Subject<void> = new Subject();
  query: string = 'test';

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

  ngAfterViewInit() {
    setTimeout(() => {
      const query = this.route.snapshot.queryParams['objectiveQuery'];
      const objectiveQuery = getQueryString(query);
      this.query = objectiveQuery;
      this.refreshService.refreshObjectiveFilter(objectiveQuery);
    }, 1000);
  }
}
