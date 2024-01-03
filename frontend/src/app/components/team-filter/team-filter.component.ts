import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, Subject, takeUntil } from 'rxjs';
import { Team } from '../../shared/types/model/Team';
import { TeamService } from '../../services/team.service';
import { ActivatedRoute, Router } from '@angular/router';
import { areEqual, getValueFromQuery, optionalReplaceWithNulls, trackByFn } from '../../shared/common';
import { RefreshDataService } from '../../services/refresh-data.service';
import { UserService } from '../../services/user.service';
import { extractTeamsFromUser } from '../../shared/types/model/User';

@Component({
  selector: 'app-team-filter',
  templateUrl: './team-filter.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamFilterComponent implements OnInit, OnDestroy {
  teams$: BehaviorSubject<Team[]> = new BehaviorSubject<Team[]>([]);
  activeTeams: number[] = [];
  protected readonly trackByFn = trackByFn;
  private unsubscribe$ = new Subject<void>();

  constructor(
    private teamService: TeamService,
    private route: ActivatedRoute,
    private router: Router,
    private refreshDataService: RefreshDataService,
    private userService: UserService,
  ) {
    this.refreshDataService.reloadOverviewSubject.subscribe(() => {
      this.teamService
        .getAllTeams()
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe((teams) => {
          this.teams$.next(teams);
          this.activeTeams = this.activeTeams.filter((teamId) => this.getAllTeamIds().includes(teamId));
          this.changeTeamFilterParams();
        });
    });
  }

  ngOnInit(): void {
    this.teamService
      .getAllTeams()
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((teams: Team[]) => {
        this.teams$.next(teams);
        const teamQuery = this.route.snapshot.queryParams['teams'];
        const teamIds = getValueFromQuery(teamQuery);
        const knownTeams = this.getAllTeamIds().filter((teamId) => teamIds?.includes(teamId));
        if (knownTeams.length == 0) {
          this.activeTeams = extractTeamsFromUser(this.userService.getCurrentUser()).map((team) => team.id);
        } else {
          this.activeTeams = knownTeams;
        }
        this.changeTeamFilterParams();
      });
  }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  changeTeamFilterParams() {
    const params = { teams: this.activeTeams.join(',') };
    const optionalParams = optionalReplaceWithNulls(params);
    this.router
      .navigate([], { queryParams: optionalParams })
      .then(() => this.refreshDataService.teamFilterReady.next());
  }

  toggleSelection(id: number) {
    if (this.areAllTeamsShown()) {
      this.activeTeams = [id];
    } else if (this.activeTeams.includes(id)) {
      this.activeTeams = this.activeTeams.filter((teamId) => teamId !== id);
    } else {
      this.activeTeams.push(id);
    }

    this.changeTeamFilterParams();
  }

  areAllTeamsShown() {
    return areEqual(this.activeTeams, this.getAllTeamIds());
  }

  toggleAll() {
    this.activeTeams = this.areAllTeamsShown() ? [] : this.getAllTeamIds();
    this.changeTeamFilterParams();
  }

  getAllTeamIds() {
    return this.teams$.getValue().map((team) => team.id);
  }
}