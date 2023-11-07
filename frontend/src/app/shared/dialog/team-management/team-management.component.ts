import { Component } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-team-management',
  templateUrl: './team-management.component.html',
  styleUrls: ['./team-management.component.css'],
})
export class TeamManagementComponent {
  constructor(
    public dialogRef: MatDialogRef<TeamManagementComponent>,
    private dialog: MatDialog,
  ) {}
}
