import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';

const routes: Routes = [
  { path: '', component: DashboardComponent, pathMatch: 'full' },
  {
    path: 'keyresults/edit/:id',
    loadChildren: () =>
      import('./key-result-form/key-result-form.module').then(
        (m) => m.KeyResultFormModule
      ),
  },
  {
    path: 'keyresults/edit',
    redirectTo: 'keyresults/edit/',
    pathMatch: 'full',
  },
  { path: '**', component: DashboardComponent, pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
