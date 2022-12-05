import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamListComponent } from './team-list/team-list.component';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { TeamFormComponent } from './team-form/team-form.component';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [TeamListComponent, TeamFormComponent],
  exports: [TeamListComponent],
  imports: [
    CommonModule,
    MatDividerModule,
    MatButtonModule,
    RouterLink,
    FormsModule,
  ],
})
export class TeamModule {}
