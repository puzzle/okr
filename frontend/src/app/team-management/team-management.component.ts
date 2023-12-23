import {Component} from '@angular/core';
import {isMobileDevice} from "../shared/common";
import {AddEditTeamDialog} from "./add-edit-team-dialog/add-edit-team-dialog.component";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-team-management',
  templateUrl: './team-management.component.html',
  styleUrl: './team-management.component.css'
})
export class TeamManagementComponent {

  private dialogRef!: MatDialogRef<AddEditTeamDialog> | undefined;

  public constructor(
    private dialog: MatDialog,
  ) {}

  openTeamManagement() {
    const dialogConfig = isMobileDevice()
      ? {
        maxWidth: '100vw',
        maxHeight: '100vh',
        height: '100vh',
        width: '100vw',
      }
      : {
        width: '45em',
        height: 'auto',
      };
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
