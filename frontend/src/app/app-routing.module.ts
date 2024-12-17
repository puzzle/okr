import { inject, NgModule } from '@angular/core';
import { ResolveFn, RouterModule, Routes } from '@angular/router';
import { OverviewComponent } from './components/overview/overview.component';
import { of } from 'rxjs';
import { SidePanelComponent } from './shared/side-panel/side-panel.component';
import { authGuard } from './guards/auth.guard';
import { UserService } from './services/user.service';
import { User } from './shared/types/model/User';
import { OAuthService } from 'angular-oauth2-oidc';
import { ObjectiveDetailComponent } from './components/objective-detail/objective-detail.component';
import { KeyresultDetailComponent } from './components/keyresult-detail/keyresult-detail.component';

const currentUserResolver: ResolveFn<User | undefined> = () => {
  const oauthService = inject(OAuthService);
  const userService = inject(UserService);
  if (oauthService.hasValidIdToken()) {
    return userService.getOrInitCurrentUser();
  }
  return of(undefined);
};

const routes: Routes = [
  {
    path: '',
    component: OverviewComponent,
    resolve: {
      user: currentUserResolver,
    },
    children: [
      {
        path: 'details',
        component: SidePanelComponent,
        children: [
          {
            path: 'objective/:id',
            component: ObjectiveDetailComponent,
          },
          {
            path: 'keyresult/:id',
            component: KeyresultDetailComponent,
          },
        ],
      },
    ],
    canActivate: [authGuard],
  },
  {
    path: 'team-management',
    loadChildren: () => import('./team-management/team-management.module').then((m) => m.TeamManagementModule),
    canActivate: [authGuard],
    resolve: { user: currentUserResolver },
  },
  { path: 'objective', redirectTo: 'details/objective' },
  { path: 'keyresult', redirectTo: 'details/keyresult' },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
