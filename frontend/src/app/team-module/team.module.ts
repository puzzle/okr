import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamListComponent } from './team-list/team-list.component';
import { MatDividerModule } from '@angular/material/divider';

@NgModule({
  declarations: [TeamListComponent],
  exports: [TeamListComponent],
  imports: [CommonModule, MatDividerModule],
})
export class TeamModule {}
