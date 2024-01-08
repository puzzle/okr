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
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { SearchTeamManagementComponent } from './search-team-management/search-team-management.component';
import { AddMemberToTeamDialogComponent } from './add-member-to-team-dialog/add-member-to-team-dialog.component';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MemberDetailComponent } from './member-detail/member-detail.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TeamRoleDropdownComponent } from './team-role-dropdown/team-role-dropdown.component';
import { MatSelectModule } from '@angular/material/select';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [
    TeamManagementComponent,
    AddEditTeamDialog,
    TeamManagementBannerComponent,
    TeamListComponent,
    MemberListComponent,
    RolesPipe,
    TeamsPipe,
    SearchTeamManagementComponent,
    AddMemberToTeamDialogComponent,
    MemberDetailComponent,
    TeamRoleDropdownComponent,
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
    MatIconModule,
    MatInputModule,
    MatButtonModule,
    MatMenuModule,
    MatAutocompleteModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    TranslateModule,
  ],
})
export class TeamManagementModule {}
