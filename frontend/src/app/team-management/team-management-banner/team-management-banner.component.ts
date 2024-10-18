import { Component } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { AddEditTeamDialog } from '../add-edit-team-dialog/add-edit-team-dialog.component';

@Component({
  selector: 'app-team-management-banner',
  templateUrl: './team-management-banner.component.html',
  styleUrl: './team-management-banner.component.scss',
})
export class TeamManagementBannerComponent {
  private dialogRef!: MatDialogRef<AddEditTeamDialog> | undefined;

  public constructor(private dialogService: MatDialog) {}

  createTeam(): void {
    if (!this.dialogRef) {
      this.dialogRef = this.dialogService.open(AddEditTeamDialog);
      this.dialogRef.afterClosed().subscribe(() => {
        this.dialogRef = undefined;
      });
    }
  }
}
