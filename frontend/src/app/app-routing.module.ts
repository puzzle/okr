import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ObjectiveColumnComponent } from './objective/objective-column/objective-column.component';

const routes: Routes = [
  {
    path: 'objectivestest',
    component: ObjectiveColumnComponent,
  },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
