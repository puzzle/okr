import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { AddEditTeamDialogComponent } from '../add-edit-team-dialog/add-edit-team-dialog.component';
import { DialogService } from '../../services/dialog.service';

@Component({
  selector: 'app-team-management-banner',
  templateUrl: './team-management-banner.component.html',
  styleUrl: './team-management-banner.component.scss',
  standalone: false
})
export class TeamManagementBannerComponent {
  private dialogRef!: MatDialogRef<AddEditTeamDialogComponent> | undefined;

  public constructor(private dialogService: DialogService) {}

  createTeam(): void {
    if (!this.dialogRef) {
      this.dialogRef = this.dialogService.open(AddEditTeamDialogComponent);
      this.dialogRef.afterClosed()
        .subscribe(() => {
          this.dialogRef = undefined;
        });
    }
  }
}
