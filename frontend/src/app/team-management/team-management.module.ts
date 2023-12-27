import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { TeamManagementComponent } from './team-management.component';
import { TeamManagementRoutingModule } from './team-management-routing.module';
import { AddEditTeamDialog } from './add-edit-team-dialog/add-edit-team-dialog.component';
import { SharedModule } from '../shared/shared.module';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TeamManagementBannerComponent } from './team-management-banner/team-management-banner.component';

@NgModule({
  declarations: [TeamManagementComponent, AddEditTeamDialog, TeamManagementBannerComponent],
  imports: [
    CommonModule,
    MatDialogModule,
    TeamManagementRoutingModule,
    MatFormFieldModule,
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    NgOptimizedImage,
  ],
})
export class TeamManagementModule {}
