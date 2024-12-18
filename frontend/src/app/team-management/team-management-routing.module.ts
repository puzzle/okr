import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TeamManagementComponent } from './team-management.component';
import { SidePanelComponent } from '../shared/side-panel/side-panel.component';
import { MemberDetailComponent } from './member-detail/member-detail.component';

const children = [
  {
    path: 'details',
    component: SidePanelComponent,
    children: [
      {
        path: 'member/:id',
        component: MemberDetailComponent,
      },
    ],
  },
];

const routes: Routes = [
  {
    path: '',
    component: TeamManagementComponent,
    children,
  },
  {
    path: ':teamId',
    component: TeamManagementComponent,
    children,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TeamManagementRoutingModule {}
