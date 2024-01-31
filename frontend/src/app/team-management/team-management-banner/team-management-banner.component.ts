import { Component } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { AddEditTeamDialog } from '../add-edit-team-dialog/add-edit-team-dialog.component';
import { OKR_DIALOG_CONFIG } from '../../shared/constantLibary';

@Component({
  selector: 'app-team-management-banner',
  templateUrl: './team-management-banner.component.html',
  styleUrl: './team-management-banner.component.scss',
})
export class TeamManagementBannerComponent {
  private dialogRef!: MatDialogRef<AddEditTeamDialog> | undefined;

  public constructor(private dialog: MatDialog) {}

  createTeam(): void {
    if (!this.dialogRef) {
      const config = OKR_DIALOG_CONFIG;
      config.data = undefined;
      this.dialogRef = this.dialog.open(AddEditTeamDialog, config);
      this.dialogRef.afterClosed().subscribe(() => {
        this.dialogRef = undefined;
      });
    }
  }
}
