import { NgModule } from "@angular/core";
import { CommonModule, NgOptimizedImage } from "@angular/common";
import { TeamManagementComponent } from "./team-management.component";
import { TeamManagementRoutingModule } from "./team-management-routing.module";
import { AddEditTeamDialog } from "./add-edit-team-dialog/add-edit-team-dialog.component";
import { SharedModule } from "../shared/shared.module";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { TeamManagementBannerComponent } from "./team-management-banner/team-management-banner.component";
import { TeamListComponent } from "./team-list/team-list.component";
import { MemberListComponent } from "./member-list/member-list.component";
import { MatListModule } from "@angular/material/list";
import { MatTableModule } from "@angular/material/table";
import { RolesPipe } from "./roles.pipe";
import { TeamsPipe } from "./teams.pipe";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { MatMenuModule } from "@angular/material/menu";
import { SearchTeamManagementComponent } from "./search-team-management/search-team-management.component";
import { AddMemberToTeamDialogComponent } from "./add-member-to-team-dialog/add-member-to-team-dialog.component";
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { MemberDetailComponent } from "./member-detail/member-detail.component";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { TeamRoleDropdownComponent } from "./team-role-dropdown/team-role-dropdown.component";
import { MatSelectModule } from "@angular/material/select";
import { TranslateModule } from "@ngx-translate/core";
import { AddUserTeamComponent } from "./add-user-team/add-user-team.component";
import { A11yModule } from "@angular/cdk/a11y";
import { TeamManagementMobileFilterComponent } from "./team-management-mobile-filter/team-management-mobile-filter.component";
import { MemberListTableComponent } from "./member-list/member-list-table/member-list-table.component";
import { MemberListMobileComponent } from "./member-list/member-list-mobile/member-list-mobile.component";
import { OkrChampionPipe } from "./okr-champion.pipe";
import { PuzzleIconComponent } from "../shared/custom/puzzle-icon/puzzle-icon.component";
import { PuzzleIconButtonComponent } from "../shared/custom/puzzle-icon-button/puzzle-icon-button.component";
import { ShowEditRoleComponent } from "./show-edit-role/show-edit-role.component";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { EditOkrChampionComponent } from "./edit-okr-champion/edit-okr-champion.component";
import { MatTooltipModule } from "@angular/material/tooltip";
import { NewUserComponent } from "./new-user/new-user.component";
import { InviteUserDialogComponent } from "./invite-user-dialog/invite-user-dialog.component";
import { DeleteUserComponent } from "./delete-user/delete-user.component";
import { MatChip } from "@angular/material/chips";

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
    AddUserTeamComponent,
    DeleteUserComponent,
    TeamManagementMobileFilterComponent,
    MemberListTableComponent,
    MemberListMobileComponent,
    PuzzleIconComponent,
    PuzzleIconButtonComponent,
    ShowEditRoleComponent,
    EditOkrChampionComponent,
    NewUserComponent,
    InviteUserDialogComponent
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
    A11yModule,
    OkrChampionPipe,
    MatCheckboxModule,
    MatTooltipModule,
    MatChip
  ]
})
export class TeamManagementModule {}
