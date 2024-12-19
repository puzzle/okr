import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from "@angular/core";
import { Team } from "../../shared/types/model/Team";
import { TeamService } from "../../services/team.service";
import { combineLatest, map, Observable, Subject, takeUntil } from "rxjs";
import { UserTeam } from "../../shared/types/model/UserTeam";

@Component({
  selector: "app-add-user-team",
  templateUrl: "./add-user-team.component.html",
  styleUrl: "./add-user-team.component.scss"
})
export class AddUserTeamComponent implements OnInit, OnDestroy {
  @Output()
  addUserTeam = new EventEmitter<UserTeam>();

  @Input({ required: true })
  currentTeams$!: Observable<UserTeam[]>;

  userTeam: { team: Team;
    isTeamAdmin: boolean; } | undefined;

  selectableAdminTeams$: Observable<Team[]> | undefined;

  allAdminTeams$: Observable<Team[]> | undefined;

  private unsubscribe$ = new Subject<void>();

  constructor (private readonly teamService: TeamService) {}

  ngOnInit () {
    this.allAdminTeams$ = this.teamService.getAllTeams()
      .pipe(takeUntil(this.unsubscribe$), map((teams) => {
        return teams.filter((t) => t.writeable);
      }));

    this.selectableAdminTeams$ = combineLatest([this.allAdminTeams$,
      this.currentTeams$])
      .pipe(takeUntil(this.unsubscribe$), map(([allTeams,
        userTeams]) => {
        const currentTeamIds = userTeams.map((ut) => ut.team.id);
        return allTeams.filter((t) => {
          return !currentTeamIds.includes(t.id);
        });
      }));
  }

  ngOnDestroy () {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  createUserTeam (team: Team) {
    this.userTeam = {
      team,
      isTeamAdmin: false
    };
  }

  save (): void {
    if (!this.userTeam) {
      throw new Error("UserTeam should be defined here");
    }
    this.addUserTeam.next(this.userTeam);
    this.userTeam = undefined;
  }

  showAddButton (adminTeams: Team[] | null) {
    return !this.userTeam && adminTeams?.length;
  }

  addButtonDisabled (selectableAdminTeams: Team[] | null) {
    return !selectableAdminTeams?.length;
  }
}
