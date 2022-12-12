import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamDetailComponent } from './team-detail/team-detail.component';
import { TeamService } from '../shared/services/team.service';
import { MatExpansionModule } from '@angular/material/expansion';
import { ObjectiveModule } from '../objective/objective.module';
import { MatDividerModule } from '@angular/material/divider';
import { TeamListComponent } from './team-list/team-list.component';
import { TeamFormComponent } from './team-form/team-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { MatInputModule } from '@angular/material/input';

@NgModule({
  declarations: [TeamDetailComponent, TeamListComponent, TeamFormComponent],
  providers: [TeamService],
  exports: [TeamDetailComponent, TeamListComponent],
  imports: [
    CommonModule,
    MatExpansionModule,
    ObjectiveModule,
    MatDividerModule,
    FormsModule,
    MatButtonModule,
    RouterLink,
    ReactiveFormsModule,
    MatInputModule,
  ],
})
export class TeamModule {}
