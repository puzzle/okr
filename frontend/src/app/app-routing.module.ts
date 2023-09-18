import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OverviewComponent } from './overview/overview.component';
import { ObjectiveDetailComponent } from './objective-detail/objective-detail.component';
import {KeyresultDetailComponent} from "./keyresult-detail/keyresult-detail.component";

const routes: Routes = [
  {
    path: '',
    component: OverviewComponent,
    children: [
      { path: 'objective/:id', component: ObjectiveDetailComponent, pathMatch: 'full' },
      { path: 'keyresult/:id', component: KeyresultDetailComponent, pathMatch: 'full' }
    ],
  },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
