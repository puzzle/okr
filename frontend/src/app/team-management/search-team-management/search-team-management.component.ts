import { Component } from "@angular/core";
import { FormControl } from "@angular/forms";
import { UserService } from "../../services/user.service";
import { TeamService } from "../../services/team.service";
import { Team } from "../../shared/types/model/Team";
import { User } from "../../shared/types/model/User";
import { BehaviorSubject, combineLatest, debounceTime, distinctUntilChanged, map } from "rxjs";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import { getRouteToTeam, getRouteToUserDetails } from "../../shared/routeUtils";
import { ActivatedRoute, Router } from "@angular/router";

export interface FilteredUser extends User {
  displayValue: string;
  htmlValue: string;
}

export interface FilteredTeam extends Team {
  displayValue: string;
  htmlValue: string;
}

@Component({
  selector: "app-search-team-management",
  templateUrl: "./search-team-management.component.html",
  styleUrl: "./search-team-management.component.scss",
})
export class SearchTeamManagementComponent {
  static MAX_SUGGESTIONS = 3;
  search = new FormControl("");

  filteredUsers$ = new BehaviorSubject<FilteredUser[]>([]);
  filteredTeams$ = new BehaviorSubject<FilteredTeam[]>([]);
  searchValue$ = new BehaviorSubject<string>("");

  private teams: Team[] = [];
  private users: User[] = [];

  constructor(
    private readonly userService: UserService,
    private readonly teamService: TeamService,
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute,
  ) {
    combineLatest([teamService.getAllTeams(), userService.getUsers()])
      .pipe(takeUntilDestroyed())
      .subscribe(([teams, users]) => {
        this.updateTeamsAndUsers(teams, users);
        this.applyFilter(this.searchValue$.getValue());
      });

    this.search.valueChanges
      .pipe(
        takeUntilDestroyed(), debounceTime(200), map((v) => (v ? v.trim() : "")), distinctUntilChanged(),
      )
      .subscribe((searchValue) => {
        this.searchValue$.next(searchValue);
      });

    this.searchValue$.pipe(takeUntilDestroyed())
      .subscribe(() => {
        this.applyFilter(this.searchValue$.getValue());
      });
  }

  selectUser(user: User) {
    this.search.setValue("");
    const teamId: number = this.activatedRoute.snapshot.params["teamId"];
    this.router.navigateByUrl(getRouteToUserDetails(user.id, teamId))
      .then();
  }

  selectTeam(team: Team) {
    this.search.setValue("");
    this.router.navigateByUrl(getRouteToTeam(team.id))
      .then();
  }

  private applyFilter(filterValue: string): void {
    if (!filterValue.length) {
      this.filteredUsers$.next([]);
      this.filteredTeams$.next([]);
      return;
    }

    this.filteredTeams$.next(
      this.filterTeams(this.teams, filterValue)
        .sort((a, b) => this.sortByStringPosition(a.displayValue, b.displayValue, filterValue))
        .slice(0, SearchTeamManagementComponent.MAX_SUGGESTIONS),
    );
    this.filteredUsers$.next(
      this.filterUsers(this.users, filterValue)
        .sort((a, b) => this.sortByStringPosition(a.displayValue, b.displayValue, filterValue))
        .slice(0, SearchTeamManagementComponent.MAX_SUGGESTIONS),
    );
  }

  private sortByStringPosition(a: string, b: string, value: string): number {
    const indexA = a.toLowerCase()
      .indexOf(value);
    const indexB = b.toLowerCase()
      .indexOf(value);
    if (indexA === indexB) {
      return 0;
    }

    if (indexB === -1) {
      return -1;
    }

    if (indexA === -1) {
      return 1;
    }

    return indexA - indexB;
  }

  private updateTeamsAndUsers(teams: Team[], users: User[]) {
    this.teams = [...teams].sort((a, b) => a.name.localeCompare(b.name));
    this.users = users.sort((a, b) => (a.firstname + a.lastname).localeCompare(b.firstname + b.lastname));
    this.applyFilter(this.searchValue$.getValue());
  }

  private filterTeams(teams: Team[], filterValue: string): FilteredTeam[] {
    return teams
      .filter((team) => this.containsText(team.name, filterValue))
      .map((team) => ({
        ...team,
        displayValue: team.name,
        htmlValue: this.formatText(team.name, filterValue),
      }));
  }

  private filterUsers(users: User[], filterValue: string): FilteredUser[] {
    return users
      .filter((user) => this.containsText(user.firstname + user.lastname + user.email, filterValue))

      .map((user) => ({
        ...user,
        displayValue: `${user.firstname} ${user.lastname} (${user.email})`,
        htmlValue: this.formatText(`${user.firstname} ${user.lastname} (${user.email})`, filterValue),
      }));
  }

  private containsText(value: string, text: string): boolean {
    return value.toLowerCase()
      .indexOf(text.toLowerCase()) >= 0;
  }

  private formatText(value: string, text: string): string {
    return value.replaceAll(new RegExp(`(${text})`, "ig"), "<strong>$1</strong>");
  }
}
