import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Team, TeamService } from '../shared/services/team.service';
import {
  BehaviorSubject,
  filter,
  first,
  map,
  Observable,
  pluck,
  Subject,
  switchMap,
  tap,
} from 'rxjs';
import { Quarter, QuarterService } from '../shared/services/quarter.service';
import { Overview, OverviewService } from '../shared/services/overview.service';
import { RouteService } from '../shared/services/route.service';
import { ActivatedRoute } from '@angular/router';
import { getNumberOrNull } from '../shared/common';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent implements OnInit {
  filters = new FormGroup({
    teamsFilter: new FormControl<number[]>([]),
    quarterFilter: new FormControl<number>(0),
  });
  teams$!: Observable<Team[]>;
  overview$: Subject<Overview[]> = new BehaviorSubject<Overview[]>([]);
  quarters$!: Observable<Quarter[]> | undefined;

  constructor(
    private teamService: TeamService,
    private quarterService: QuarterService,
    private overviewService: OverviewService,
    private routeService: RouteService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.teams$ = this.teamService.getTeams();
    this.quarters$ = this.quarterService.getQuarters();
    //select filter values from url stored in params
    this.route.queryParams
      .pipe(
        switchMap((params) =>
          this.quarters$!.pipe(
            // go through quarter$ Observable<Quarter[]>
            map((quarters) => {
              // get value of quarterFilter or undefined from FormControl inside FormGroup
              const quarterFilterControl = this.filters.get('quarterFilter');
              // define defautlQuarter as current Quarter (firs quarter in this.quarters$)
              const defaultQuarter = quarters[0].id;
              //if we load website new, console.log should show: "undefined", if we refresh, console.log should show "1"
              console.log(
                '(1): this are the quarter filter params ==>',
                params['quarterFilter']
              );
              // check if no value is set to quarterFilter FormControl, if quarters is a list of multiple objects
              // and check if there is no values stored in the URL for 'quarterFilter'
              if (
                !quarterFilterControl?.value &&
                quarters.length > 0 &&
                !params['quarterFilter']
              ) {
                // set FormControl value to defaultQuarter
                this.filters.get('quarterFilter')!.setValue(defaultQuarter);
                // change URL to defaultQuarter --> URL should be /?quarterFilter=1
                this.changeQuarterFilter(defaultQuarter);
                //if we load website new, console.log should show: "Object{teamsFilter: [], quarterFilter: 1}"
                //if we refresh, console.log should not appear because if statement should not be called
                console.log('(2): value of Formgroups', this.filters.value);
              } else {
                // should keep the set Filter if we reload the website
                this.filters
                  .get('quarterFilter')!
                  .setValue(params['quarterFilter']);
              }
              // console.log should always show: "Object{teamsFilter: [], quarterFilter: 1}"
              console.log(
                '(3): this filters value are ==>',
                this.filters.value
              );
              // console.log should always show: "undefined 1", but it only shows after refreshing website
              console.log(
                '(4): this are the set params',
                params['teamFilter'],
                params['quarterFilter']
              );
              // return params, so we can use the params for the teamFilter later below
              return params;
            })
          )
        ),
        map((params) => {
          // should get an array of the ids from the selected teams
          let selectedTeams: number[] = [];
          (params['teamFilter']?.split(',') ?? []).forEach((item: string) =>
            selectedTeams.push(getNumberOrNull(item)!)
          );
          // console.log should show: "Array [] 1" but only shows after refreshing the website
          console.log(
            '(5): these are the values we want to set to the filters ==> ',
            selectedTeams,
            params['quarterFilter']
          );
          // should set the final filters including teamsFilter
          this.filters.setValue({
            teamsFilter: selectedTeams,
            quarterFilter: params['quarterFilter'],
          });
          // changes URL if any teams are selected
          this.changeTeamFilter(selectedTeams);
        })
      )
      .subscribe();
  }

  changeTeamFilter(value: number[]) {
    this.routeService.changeTeamFilter(value).subscribe(() => {
      this.reloadOverview();
    });
  }

  changeQuarterFilter(value: number) {
    // to check if Url will be changed
    console.log('(1.5): this is the change quarter event handler ');
    this.routeService.changeQuarterFilter(value).subscribe(() => {
      this.reloadOverview();
    });
  }

  reloadOverview() {
    // shows when the api call is made and which values are passed to overview.service.ts
    console.log(
      '(1.75 / 6): these are the filters in the reload overview ==>',
      this.filters.value
    );
    this.route.queryParams
      .subscribe((params) => {
        this.overviewService
          .getOverview(params['quarterFilter'], params['teamFilter'] ?? [])
          .subscribe((data) => {
            this.overview$.next(data);
          });
      })
      .unsubscribe();
  }
}
