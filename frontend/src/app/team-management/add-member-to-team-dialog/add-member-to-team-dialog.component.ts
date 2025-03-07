import { Component, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { BehaviorSubject, combineLatest, filter, map, Observable, startWith, Subject, takeUntil } from 'rxjs';
import { Team } from '../../shared/types/model/team';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { User } from '../../shared/types/model/user';
import { UserService } from '../../services/user.service';
import { FormControl } from '@angular/forms';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { TeamService } from '../../services/team.service';
import { UserTableEntry } from '../../shared/types/model/user-table-entry';

export interface AddMemberToTeamDialogComponentData {
  team: Team;
  currentUsersOfTeam: UserTableEntry[];
}

@Component({
  selector: 'app-add-member-to-team-dialog',
  templateUrl: './add-member-to-team-dialog.component.html',
  styleUrl: './add-member-to-team-dialog.component.scss',
  standalone: false
})
export class AddMemberToTeamDialogComponent implements OnInit, OnDestroy {
  @ViewChild(MatTable) table!: MatTable<User[]>;

  selectedUsers$: BehaviorSubject<User[]> = new BehaviorSubject<User[]>([]);

  search = new FormControl('');

  usersForSelection$: Observable<User[]> | undefined;

  displayedColumns = ['name',
    'delete'];

  dataSource: MatTableDataSource<User> | undefined;

  private readonly unsubscribe$ = new Subject<void>();

  public constructor(
    private readonly userService: UserService,
    private readonly teamService: TeamService,
    public dialogRef: MatDialogRef<AddMemberToTeamDialogComponent>,
    @Inject(MAT_DIALOG_DATA)
    public data: AddMemberToTeamDialogComponentData
  ) {
    this.selectedUsers$.subscribe((users) => this.dataSource = new MatTableDataSource<User>(users));
  }

  public ngOnInit(): void {
    // directly after selecting object, filtervalue is an object.
    this.usersForSelection$ = combineLatest([this.userService.getUsers(),
      this.selectedUsers$,
      this.search.valueChanges.pipe(startWith(''), filter((searchValue) => typeof searchValue === 'string'))])
      .pipe(takeUntil(this.unsubscribe$), map(([allPossibleUsers,
        selectedUsers,
        filterValue]) => {
        return this.filter(allPossibleUsers, filterValue || '', selectedUsers);
      }));
  }

  public ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  getDialogTitle(): string {
    return `Members zu Team ${this.data.team.name} hinzufügen`;
  }

  addUsersToTeam(): void {
    this.teamService.addUsersToTeam(this.data.team, this.selectedUsers$.getValue())
      .subscribe(() => {
        this.userService.reloadUsers();
        this.userService.reloadCurrentUser()
          .subscribe();
        this.dialogRef.close();
      });
  }

  private filter(allPossibleUsers: User[], searchValue: string, selectedUsers: User[]) {
    // filter currently current users of team
    const currentUserIds = this.data.currentUsersOfTeam.map((u) => u.id);
    const filteredUsers = allPossibleUsers.filter((u) => !currentUserIds.includes(u.id));
    const filterLower = searchValue.toLowerCase();
    return filteredUsers.filter((user) => {
      // already added users should not be displayed
      if (selectedUsers.find((u) => u.id === user.id)) {
        return false;
      }
      return this.getDisplayValue(user)
        .toLowerCase()
        .includes(filterLower);
    });
  }

  getDisplayValue(user: User | ''): string {
    if (!user) {
      return '';
    }
    return `${user.firstName} ${user.lastName} (${user.email})`;
  }

  selectUser(user: User) {
    const newUsers = this.selectedUsers$.getValue();
    newUsers.push(user);
    this.selectedUsers$.next(newUsers);
    this.search.setValue('');
  }

  remove(user: User): void {
    const filteredUsers = this.selectedUsers$.getValue()
      .filter((u) => u !== user);
    this.selectedUsers$.next(filteredUsers);
  }
}
