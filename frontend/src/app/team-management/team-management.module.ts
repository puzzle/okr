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
import { TeamListComponent } from './team-list/team-list.component';
import { MemberListComponent } from './member-list/member-list.component';
import { MatListModule } from '@angular/material/list';
import { MatTableModule } from '@angular/material/table';
import { RolesPipe } from './roles.pipe';
import { TeamsPipe } from './teams.pipe';

@NgModule({
  declarations: [
    TeamManagementComponent,
    AddEditTeamDialog,
    TeamManagementBannerComponent,
    TeamListComponent,
    MemberListComponent,
    RolesPipe,
    TeamsPipe,
  ],
  imports: [
    CommonModule,
    MatDialogModule,
    TeamManagementRoutingModule,
    MatFormFieldModule,
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    NgOptimizedImage,
    MatListModule,
    MatTableModule,
  ],
})
export class TeamManagementModule {}
