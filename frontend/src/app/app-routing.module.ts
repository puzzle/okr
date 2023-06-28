import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './release-1/dashboard/dashboard.component';
import { KeyresultFormComponent } from './release-1/shared/components/shared/keyresult-form/keyresult-form.component';
import { TeamListComponent } from './release-1/team/team-list/team-list.component';
import { ObjectiveFormComponent } from './release-1/objective/objective-form/objective-form.component';
import { TeamFormComponent } from './release-1/team/team-form/team-form.component';
import { MeasureFormComponent } from './release-1/shared/components/shared/measure-form/measure-form.component';
import { KeyResultDetailComponent } from './release-1/shared/components/shared/key-result-detail/key-result-detail.component';

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
  { path: 'teams', component: TeamListComponent },
  { path: 'keyresults/:keyresultId', component: KeyResultDetailComponent },
  {
    path: 'keyresults/:keyresultId/measure/new',
    component: MeasureFormComponent,
  },
  {
    path: 'keyresults/:keyresultId/measure/edit/:measureId',
    component: MeasureFormComponent,
  },

  { path: 'team/:teamId/objectives/new', component: ObjectiveFormComponent },
  { path: 'objectives/edit/:objectiveId', component: ObjectiveFormComponent },
  { path: 'team/edit/:id', component: TeamFormComponent },
  { path: 'team/edit', component: TeamFormComponent },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
