import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy } from "@angular/core";
import { UserService } from "../../services/user.service";
import { ActivatedRoute, Router } from "@angular/router";
import { BehaviorSubject, combineLatest, filter, map, mergeMap, ReplaySubject, Subject, takeUntil } from "rxjs";
import { User } from "../../shared/types/model/User";
import { convertFromUsers, UserTableEntry } from "../../shared/types/model/UserTableEntry";
import { TeamService } from "../../services/team.service";
import { Team } from "../../shared/types/model/Team";
import { AddMemberToTeamDialogComponent } from "../add-member-to-team-dialog/add-member-to-team-dialog.component";
import { AddEditTeamDialogComponent } from "../add-edit-team-dialog/add-edit-team-dialog.component";
import { MatTableDataSource } from "@angular/material/table";
import { InviteUserDialogComponent } from "../invite-user-dialog/invite-user-dialog.component";
import { DialogService } from "../../services/dialog.service";

@Component({
  selector: "app-member-list",
  templateUrl: "./member-list.component.html",
  styleUrl: "./member-list.component.scss"
})
export class MemberListComponent implements OnDestroy, AfterViewInit {
  dataSource: MatTableDataSource<UserTableEntry> = new MatTableDataSource<UserTableEntry>([]);

  selectedTeam$: BehaviorSubject<Team | undefined> = new BehaviorSubject<Team | undefined>(undefined);

  private allUsersSubj: ReplaySubject<User[]> = new ReplaySubject<User[]>(1);

  private unsubscribe$ = new Subject<void>();

  public constructor (
    private readonly userService: UserService,
    private readonly route: ActivatedRoute,
    private readonly cd: ChangeDetectorRef,
    private readonly teamService: TeamService,
    private readonly router: Router,
    private readonly dialogService: DialogService
  ) {}

  public ngAfterViewInit () {
    this.userService
      .getUsers()
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((users) => this.allUsersSubj.next(users));
    const teamId$ = this.route.paramMap.pipe(map((params) => params.get("teamId")));
    combineLatest([this.allUsersSubj.asObservable(),
      teamId$,
      this.teamService.getAllTeams()])
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(([users,
        teamIdParam,
        teams]) => {
        this.setSelectedTeam(teams, teamIdParam);
        this.setDataSourceForTeamOrAll(users, teamIdParam);
      });
  }

  public ngOnDestroy (): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  private setSelectedTeam (teams: Team[], teamIdParam: string | null) {
    if (!teamIdParam) {
      this.selectedTeam$.next(undefined);
      return;
    }
    const team = teams.find((t) => t.id === parseInt(teamIdParam));
    this.selectedTeam$.next(team);
    this.cd.markForCheck();
  }

  private setDataSourceForTeamOrAll (users: User[], teamIdParam: string | null) {
    if (!teamIdParam) {
      this.setDataSourceForAllTeams(users);
      this.cd.markForCheck();
      return;
    }
    this.setDataSourceForTeam(teamIdParam, users);
    this.cd.markForCheck();
  }

  private setDataSourceForAllTeams (users: User[]) {
    this.dataSource.data = convertFromUsers(users, null);
  }

  private setDataSourceForTeam (teamIdParam: string, users: User[]) {
    const teamId = parseInt(teamIdParam);
    this.dataSource.data = convertFromUsers(users, teamId);
  }

  deleteTeam (selectedTeam: Team) {
    const data = {
      team: selectedTeam.name
    };

    this.dialogService
      .openConfirmDialog("CONFIRMATION.DELETE.TEAM", data)
      .afterClosed()
      .pipe(filter((confirm) => confirm), mergeMap(() => this.teamService.deleteTeam(selectedTeam.id)))
      .subscribe(() => {
        this.userService.reloadUsers();
        this.userService.reloadCurrentUser()
          .subscribe();
        this.router.navigateByUrl("team-management");
      });
  }

  addMemberToTeam () {
    const dialogRef = this.dialogService.open(AddMemberToTeamDialogComponent, {
      data: {
        team: this.selectedTeam$.value,
        currentUsersOfTeam: this.dataSource.data
      }
    });
    dialogRef.afterClosed()
      .subscribe(() => this.cd.markForCheck());
  }

  inviteMember () {
    this.dialogService.open(InviteUserDialogComponent)
      .afterClosed()
      .subscribe();
  }

  showInviteMember (): boolean {
    return !this.selectedTeam$.value && this.userService.getCurrentUser().isOkrChampion;
  }

  showAddMemberToTeam () {
    return this.selectedTeam$.value?.writeable;
  }

  editTeam (): void {
    const dialogRef = this.dialogService.open(AddEditTeamDialogComponent, { data: { team: this.selectedTeam$.value } });
    dialogRef.afterClosed()
      .subscribe(() => this.cd.markForCheck());
  }
}
