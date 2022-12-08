import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import {KeyresultFormComponent} from "./keyresult/keyresult-form/keyresult-form.component";
import { TeamListComponent } from './team/team-list/team-list.component';
import { TeamFormComponent } from './team/team-form/team-form.component';

const routes: Routes = [
  { path: '', component: DashboardComponent, pathMatch: 'full' },
  {
    path: 'objective/:objectiveId/keyresult/edit/:keyresultId',
    component: KeyresultFormComponent,
  },
  {
    path: 'objective/:objectiveId/keyresult/new',
    component: KeyresultFormComponent,
  },
  { path: '**', component: DashboardComponent, pathMatch: 'full' },
  { path: 'teams', component: TeamListComponent },
  { path: 'team/edit/:id', component: TeamFormComponent },
  { path: 'team/edit', component: TeamFormComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
