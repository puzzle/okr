import { ChangeDetectorRef, Component, inject, computed, effect } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { UserService } from '../../services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { filter, map, mergeMap } from 'rxjs';
import { convertFromUsers, UserTableEntry } from '../../shared/types/model/user-table-entry';
import { Team } from '../../shared/types/model/team';
import { AddMemberToTeamDialogComponent } from '../add-member-to-team-dialog/add-member-to-team-dialog.component';
import { AddEditTeamDialogComponent } from '../add-edit-team-dialog/add-edit-team-dialog.component';
import { MatTableDataSource } from '@angular/material/table';
import { InviteUserDialogComponent } from '../invite-user-dialog/invite-user-dialog.component';
import { DialogService } from '../../services/dialog.service';
import { Quarter } from '../../shared/types/model/quarter';
import { MatDialog } from '@angular/material/dialog';
import { ArchiveTeamDialogComponent } from '../../shared/dialog/archive-dialog/archive-dialog.component';
import { ALL_TEAMS_STATE } from '../../services/team-state.tokens';

@Component({
  selector: 'app-member-list',
  templateUrl: './member-list.component.html',
  styleUrl: './member-list.component.scss',
  standalone: false
})
export class MemberListComponent {
  private readonly userService = inject(UserService);

  private readonly route = inject(ActivatedRoute);

  private readonly cd = inject(ChangeDetectorRef);

  private readonly teamStateService = inject(ALL_TEAMS_STATE);

  private readonly router = inject(Router);

  private readonly dialogService = inject(DialogService);

  private readonly dialog = inject(MatDialog);

  private readonly teamIdParam = toSignal(this.route.paramMap.pipe(map((params) => params.get('teamId'))), { initialValue: null });

  private readonly users = toSignal(this.userService.getUsers(), { initialValue: [] });

  public readonly selectedTeam = computed(() => {
    const teamId = this.teamIdParam();
    if (!teamId) {
      return undefined;
    }

    const teams = this.teamStateService.getTeams()();
    return teams.find((t) => t.id === parseInt(teamId));
  });

  public readonly dataSource = new MatTableDataSource<UserTableEntry>([]);

  public readonly showInviteMember = computed(() => !this.selectedTeam() && this.userService.getCurrentUser().isOkrChampion);

  public readonly isTeamWriteable = computed(() => !!this.selectedTeam()?.isWriteable);

  public readonly isTeamArchived = computed(() => !!this.selectedTeam()?.markedAsArchivedAt);

  constructor() {
    effect(() => {
      const currentUsers = this.users();
      const teamId = this.teamIdParam();

      this.dataSource.data = !teamId
        ? convertFromUsers(currentUsers, null)
        : convertFromUsers(currentUsers, parseInt(teamId));
    });
  }

  deleteTeam(selectedTeam: Team) {
    const data = {
      team: selectedTeam.name
    };

    this.dialogService
      .openConfirmDialog('CONFIRMATION.DELETE.TEAM', data)
      .afterClosed()
      .pipe(filter((confirm) => confirm), mergeMap(() => this.teamStateService.deleteTeam(selectedTeam.id)))
      .subscribe(() => {
        this.userService.reloadUsers();
        this.userService.reloadCurrentUser()
          .subscribe();
        this.router.navigateByUrl('team-management');
      });
  }

  archiveTeam(selectedTeam: Team) {
    this.dialog
      .open(ArchiveTeamDialogComponent, { panelClass: 'okr-dialog-panel-small' })
      .afterClosed()
      .pipe(filter((selectedQuarter: Quarter | undefined) => !!selectedQuarter), mergeMap((selectedQuarter: Quarter) => {
        selectedTeam.markedAsArchivedAt = selectedQuarter.startDate;
        return this.teamStateService.archiveTeam(selectedTeam);
      }))
      .subscribe();
  }

  unarchiveTeam(selectedTeam: Team) {
    const data = { team: selectedTeam.name };

    this.dialogService
      .openConfirmDialog('CONFIRMATION.UNARCHIVE.TEAM', data)
      .afterClosed()
      .pipe(filter((confirm) => confirm), mergeMap(() => {
        selectedTeam.markedAsArchivedAt = null;
        return this.teamStateService.unarchiveTeam(selectedTeam.id);
      }))
      .subscribe();
  }

  addMemberToTeam() {
    const dialogRef = this.dialogService.open(AddMemberToTeamDialogComponent, {
      data: {
        team: this.selectedTeam(),
        currentUsersOfTeam: this.dataSource.data
      }
    });
    dialogRef.afterClosed()
      .subscribe(() => this.cd.markForCheck());
  }

  inviteMember() {
    this.dialogService.open(InviteUserDialogComponent)
      .afterClosed()
      .subscribe();
  }

  editTeam(): void {
    const dialogRef = this.dialogService.open(AddEditTeamDialogComponent, {
      data: { team: this.selectedTeam() }
    });
    dialogRef.afterClosed()
      .subscribe(() => this.cd.markForCheck());
  }
}
