import { Component } from '@angular/core';
import { isMobileDevice } from '../../shared/common';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { AddEditTeamDialog } from '../add-edit-team-dialog/add-edit-team-dialog.component';
import { OKR_DIALOG_CONFIG } from '../../shared/constantLibary';

@Component({
  selector: 'app-team-management-banner',
  templateUrl: './team-management-banner.component.html',
  styleUrl: './team-management-banner.component.scss',
})
export class TeamManagementBannerComponent {
  protected readonly isMobileDevice = isMobileDevice;
  private dialogRef!: MatDialogRef<AddEditTeamDialog> | undefined;

  public constructor(private dialog: MatDialog) {}

  createTeam(): void {
    const dialogConfig = OKR_DIALOG_CONFIG;
    if (!this.dialogRef) {
      this.dialogRef = this.dialog.open(AddEditTeamDialog, {
        height: dialogConfig.height,
        width: dialogConfig.width,
        maxHeight: dialogConfig.maxHeight,
        maxWidth: dialogConfig.maxWidth,
      });
      this.dialogRef.afterClosed().subscribe(() => {
        this.dialogRef = undefined;
      });
    }
  }
}
