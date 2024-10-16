import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, combineLatest, filter, map, mergeMap, ReplaySubject, Subject, takeUntil } from 'rxjs';
import { User } from '../../shared/types/model/User';
import { convertFromUsers, UserTableEntry } from '../../shared/types/model/UserTableEntry';
import { TeamService } from '../../services/team.service';
import { Team } from '../../shared/types/model/Team';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import {
  AddMemberToTeamDialogComponent,
  AddMemberToTeamDialogComponentData,
} from '../add-member-to-team-dialog/add-member-to-team-dialog.component';
import { OKR_DIALOG_CONFIG } from '../../shared/constantLibary';
import { AddEditTeamDialog } from '../add-edit-team-dialog/add-edit-team-dialog.component';
import { MatTableDataSource } from '@angular/material/table';
import { CancelDialogComponent, CancelDialogData } from '../../shared/dialog/cancel-dialog/cancel-dialog.component';
import { InviteUserDialogComponent } from '../invite-user-dialog/invite-user-dialog.component';

@Component({
  selector: 'app-member-list',
  templateUrl: './member-list.component.html',
  styleUrl: './member-list.component.scss',
})
export class MemberListComponent implements OnInit, OnDestroy, AfterViewInit {
  dataSource: MatTableDataSource<UserTableEntry> = new MatTableDataSource<UserTableEntry>([]);
  selectedTeam$: BehaviorSubject<Team | undefined> = new BehaviorSubject<Team | undefined>(undefined);

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
    this.dataSource.data = convertFromUsers(users, null);
  }

  private setDataSourceForTeam(teamIdParam: string, users: User[]) {
    const teamId = parseInt(teamIdParam);
    this.dataSource.data = convertFromUsers(users, teamId);
  }

  deleteTeam(selectedTeam: Team) {
    const dialogConfig: MatDialogConfig<CancelDialogData> = OKR_DIALOG_CONFIG;
    dialogConfig.data = {
      dialogTitle: selectedTeam.name + ' wirklich löschen?',
      dialogText: 'Soll das Team und dessen OKRs wirklich gelöscht werden?',
    };
    this.dialog
      .open(CancelDialogComponent, dialogConfig)
      .afterClosed()
      .pipe(
        filter((confirm) => confirm),
        mergeMap(() => this.teamService.deleteTeam(selectedTeam.id)),
      )
      .subscribe(() => {
        this.userService.reloadUsers();
        this.userService.reloadCurrentUser().subscribe();
        this.router.navigateByUrl('team-management');
      });
  }

  addMemberToTeam() {
    const dialogConfig: MatDialogConfig<AddMemberToTeamDialogComponentData> = OKR_DIALOG_CONFIG;
    dialogConfig.data = {
      team: this.selectedTeam$.value!,
      currentUsersOfTeam: this.dataSource.data,
    };
    const dialogRef = this.dialog.open(AddMemberToTeamDialogComponent, dialogConfig);
    dialogRef.afterClosed().subscribe(() => this.cd.markForCheck());
  }

  inviteMember() {
    this.dialog.open(InviteUserDialogComponent, OKR_DIALOG_CONFIG).afterClosed().subscribe();
  }

  showInviteMember(): boolean {
    return !this.selectedTeam$.value && this.userService.getCurrentUser().isOkrChampion;
  }

  showAddMemberToTeam() {
    return this.selectedTeam$.value?.writeable;
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
