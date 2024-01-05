import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, map, ReplaySubject, Subject, takeUntil } from 'rxjs';
import { User } from '../../shared/types/model/User';
import { convertFromUsers, UserTableEntry } from '../../shared/types/model/UserTableEntry';
import { TeamService } from '../../services/team.service';
import { Team } from '../../shared/types/model/Team';
import { MatDialog } from '@angular/material/dialog';
import { AddMemberToTeamDialogComponent } from '../add-member-to-team-dialog/add-member-to-team-dialog.component';
import { OKR_DIALOG_CONFIG } from '../../shared/constantLibary';
import { AddEditTeamDialog } from '../add-edit-team-dialog/add-edit-team-dialog.component';

@Component({
  selector: 'app-member-list',
  templateUrl: './member-list.component.html',
  styleUrl: './member-list.component.scss',
})
export class MemberListComponent implements OnInit, OnDestroy {
  dataSource: UserTableEntry[] = [];
  selectedTeam: Team | undefined;

  private allUsersSubj: ReplaySubject<User[]> = new ReplaySubject<User[]>(1);

  private unsubscribe$ = new Subject<void>();
  private allColumns = ['icon', 'name', 'roles', 'teams'];
  private teamColumns = ['icon', 'name', 'roles'];

  displayedColumns: string[] = this.allColumns;

  public constructor(
    private readonly userService: UserService,
    private readonly route: ActivatedRoute,
    private readonly cd: ChangeDetectorRef,
    private readonly teamService: TeamService,
    private readonly router: Router,
    private readonly dialog: MatDialog,
  ) {}

  public ngOnInit(): void {
    this.userService
      .getUsers()
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((users) => this.allUsersSubj.next(users));
    const teamId$ = this.route.paramMap.pipe(map((params) => params.get('teamId')));
    combineLatest([this.allUsersSubj.asObservable(), teamId$, this.teamService.getAllTeams()])
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(([users, teamIdParam, teams]) => {
        this.setDataSource(users, teamIdParam);
        this.setSelectedTeam(teams, teamIdParam);
      });
  }

  private setDataSource(users: User[], teamIdParam: string | null) {
    if (!teamIdParam) {
      this.dataSource = convertFromUsers(users, null);
      this.displayedColumns = this.allColumns;
      this.cd.markForCheck();
      return;
    }
    const teamId = parseInt(teamIdParam);
    this.dataSource = convertFromUsers(users, teamId);
    this.displayedColumns = this.teamColumns;
    this.cd.markForCheck();
  }

  public ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  private setSelectedTeam(teams: Team[], teamIdParam: string | null) {
    if (!teamIdParam) {
      this.selectedTeam = undefined;
      return;
    }
    this.selectedTeam = teams.find((t) => t.id === parseInt(teamIdParam));
    this.cd.markForCheck();
  }

  deleteTeam(selectedTeam: Team) {
    this.teamService.deleteTeam(selectedTeam.id).subscribe(() => this.router.navigateByUrl('team-management'));
  }

  addMemberToTeam() {
    const dialogConfig = OKR_DIALOG_CONFIG;
    dialogConfig.data = {
      team: this.selectedTeam,
      currentUsersOfTeam: this.dataSource,
    };
    const dialogRef = this.dialog.open(AddMemberToTeamDialogComponent, dialogConfig);
    dialogRef.afterClosed().subscribe(() => this.cd.markForCheck());
  }

  invitePerson() {
    alert('not implemented');
  }

  showInvitePerson(): boolean {
    return !this.selectedTeam;
  }

  showAddMemberToTeam() {
    return this.selectedTeam && this.selectedTeam.isWriteable;
  }

  editTeam(): void {
    const dialogConfig = OKR_DIALOG_CONFIG;
    dialogConfig.data = {
      team: this.selectedTeam,
    };
    const dialogRef = this.dialog.open(AddEditTeamDialog, dialogConfig);
    dialogRef.afterClosed().subscribe(() => this.cd.markForCheck());
  }

  getMemberDetailsLink(user: User) {
    return '/team-management/details/member/' + user.id;
  }
}
