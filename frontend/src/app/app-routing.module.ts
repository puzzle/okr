import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard-module/dashboard/dashboard.component';
import { TeamListComponent } from './team-module/team-list/team-list.component';
import { TeamFormComponent } from './team-module/team-form/team-form.component';

const routes: Routes = [
  { path: '', component: DashboardComponent, pathMatch: 'full' },
  { path: 'teams', component: TeamListComponent },
  { path: 'team/edit/:id', component: TeamFormComponent },
  { path: 'team/edit', component: TeamFormComponent },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
