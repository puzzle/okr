import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, combineLatest, map, ReplaySubject, Subject, takeUntil } from 'rxjs';
import { User } from '../../shared/types/model/User';
import { convertFromUsers, UserTableEntry } from '../../shared/types/model/UserTableEntry';
import { TeamService } from '../../services/team.service';
import { Team } from '../../shared/types/model/Team';
import { MatDialog } from '@angular/material/dialog';
import { AddMemberToTeamDialogComponent } from '../add-member-to-team-dialog/add-member-to-team-dialog.component';
import { OKR_DIALOG_CONFIG } from '../../shared/constantLibary';
import { AddEditTeamDialog } from '../add-edit-team-dialog/add-edit-team-dialog.component';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorSelectConfig } from '@angular/material/paginator';

@Component({
  selector: 'app-member-list',
  templateUrl: './member-list.component.html',
  styleUrl: './member-list.component.scss',
})
export class MemberListComponent implements OnInit, OnDestroy, AfterViewInit {
  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  dataSource: MatTableDataSource<UserTableEntry> = new MatTableDataSource<UserTableEntry>([]);
  selectedTeam$: BehaviorSubject<Team | undefined> = new BehaviorSubject<Team | undefined>(undefined);
  selectConfig: MatPaginatorSelectConfig = {};

  private allUsersSubj: ReplaySubject<User[]> = new ReplaySubject<User[]>(1);
  private unsubscribe$ = new Subject<void>();

  public constructor(
    private readonly userService: UserService,
    private readonly route: ActivatedRoute,
    private readonly cd: ChangeDetectorRef,
    private readonly teamService: TeamService,
    private readonly router: Router,
    private readonly dialog: MatDialog,
  ) {}

  public ngOnInit(): void {}

  public ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.userService
      .getUsers()
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((users) => this.allUsersSubj.next(users));
    const teamId$ = this.route.paramMap.pipe(map((params) => params.get('teamId')));
    combineLatest([this.allUsersSubj.asObservable(), teamId$, this.teamService.getAllTeams()])
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(([users, teamIdParam, teams]) => {
        this.setSelectedTeam(teams, teamIdParam);
        this.setDataSourceForTeamOrAll(users, teamIdParam);
      });
  }

  public ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  private setSelectedTeam(teams: Team[], teamIdParam: string | null) {
    if (!teamIdParam) {
      this.selectedTeam$.next(undefined);
      return;
    }
    const team = teams.find((t) => t.id === parseInt(teamIdParam));
    this.selectedTeam$.next(team);
    this.cd.markForCheck();
  }

  private setDataSourceForTeamOrAll(users: User[], teamIdParam: string | null) {
    if (!teamIdParam) {
      this.setDataSourceForAllTeams(users);
      this.cd.markForCheck();
      return;
    }
    this.setDataSourceForTeam(teamIdParam, users);
    this.cd.markForCheck();
  }

  private setDataSourceForAllTeams(users: User[]) {
    const userEntries = convertFromUsers(users, null);
    this.setDataSource(userEntries);
  }

  private setDataSourceForTeam(teamIdParam: string, users: User[]) {
    const teamId = parseInt(teamIdParam);
    const userEntries = convertFromUsers(users, teamId);
    this.setDataSource(userEntries);
  }

  private setDataSource(userEntries: UserTableEntry[]): void {
    this.dataSource.data = userEntries;
    this.paginator.firstPage();
  }

  deleteTeam(selectedTeam: Team) {
    this.teamService.deleteTeam(selectedTeam.id).subscribe(() => {
      this.userService.reloadUsers();
      this.userService.reloadCurrentUser().subscribe();
      this.router.navigateByUrl('team-management');
    });
  }

  addMemberToTeam() {
    const dialogConfig = OKR_DIALOG_CONFIG;
    dialogConfig.data = {
      team: this.selectedTeam$.value,
      currentUsersOfTeam: this.dataSource.data,
    };
    const dialogRef = this.dialog.open(AddMemberToTeamDialogComponent, dialogConfig);
    dialogRef.afterClosed().subscribe(() => this.cd.markForCheck());
  }

  invitePerson() {
    alert('not implemented');
  }

  showInvitePerson(): boolean {
    return !this.selectedTeam$.value;
  }

  showAddMemberToTeam() {
    return this.selectedTeam$.value?.isWriteable;
  }

  editTeam(): void {
    const dialogConfig = OKR_DIALOG_CONFIG;
    dialogConfig.data = {
      team: this.selectedTeam$.value,
    };
    const dialogRef = this.dialog.open(AddEditTeamDialog, dialogConfig);
    dialogRef.afterClosed().subscribe(() => this.cd.markForCheck());
  }
}
