import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { map, mergeMap, Observable, startWith } from 'rxjs';
import { Team } from '../../shared/types/model/Team';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { getFullNameFromUser, User } from '../../shared/types/model/User';
import { UserService } from '../../services/user.service';
import { FormControl } from '@angular/forms';
import { MatTable } from '@angular/material/table';
import { TeamService } from '../../services/team.service';

@Component({
  selector: 'app-add-member-to-team-dialog',
  templateUrl: './add-member-to-team-dialog.component.html',
  styleUrl: './add-member-to-team-dialog.component.scss',
})
export class AddMemberToTeamDialogComponent implements OnInit {
  @ViewChild(MatTable) table!: MatTable<User[]>;

  allPossibleUsers: User[] = [];
  selectedUsers: User[] = [];
  search = new FormControl('');
  filteredUsers$: Observable<User[]> | undefined;
  displayedColumns = ['name', 'delete'];
  getFullNameFromUser = getFullNameFromUser;

  public constructor(
    private readonly userService: UserService,
    private readonly teamService: TeamService,
    public dialogRef: MatDialogRef<AddMemberToTeamDialogComponent>,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      team: Team;
      currentUsersOfTeam: User[];
    },
  ) {
    this.userService.getUsers().subscribe((allUsers) => {
      // we filter that are already in the team
      const currentUserIds = data.currentUsersOfTeam.map((u) => u.id);
      this.allPossibleUsers = allUsers.filter((u) => !currentUserIds.includes(u.id));
    });
  }

  public ngOnInit(): void {
    this.filteredUsers$ = this.search.valueChanges.pipe(
      startWith(''),
      map((filterValue: string | null) => this.filter(filterValue || '')),
    );
  }

  getDialogTitle(): string {
    return `Members für Team ${this.data.team.name} einladen`;
  }

  addUsersToTeam(): void {
    this.teamService
      .addUsersToTeam(this.data.team, this.selectedUsers)
      .pipe(mergeMap(() => this.userService.reloadUsers()))
      .subscribe(() => this.dialogRef.close());
  }

  private filter(filterValue: string) {
    const filterLower = filterValue.toLowerCase();
    return this.allPossibleUsers.filter((user) => {
      // already added users should not be displayed
      if (this.selectedUsers.find((u) => u.id === user.id)) {
        return false;
      }
      return this.getDisplayValue(user).toLowerCase().includes(filterLower);
    });
  }

  getDisplayValue(user: User): string {
    return `${user.firstname} ${user.lastname} (${user.email})`;
  }

  selectUser(user: User) {
    this.selectedUsers.push(user);
    this.search.setValue('');
    this.table.renderRows();
  }

  remove(user: User): void {
    this.selectedUsers = this.selectedUsers.filter((u) => u !== user);
    this.table.renderRows();
  }
}
