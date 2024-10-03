import { ResolveFn, RouterModule, Routes } from '@angular/router';
import { AutoLoginPartialRoutesGuard, OidcSecurityService } from 'angular-auth-oidc-client';
import { CallbackComponent } from './callback/callback.component';
import { inject, NgModule } from '@angular/core';
import { authGuard } from './guards/auth.guard';
import { UserService } from './services/user.service';
import { User } from './shared/types/model/User';
import { ObjectiveDetailComponent } from './components/objective-detail/objective-detail.component';
import { KeyresultDetailComponent } from './components/keyresult-detail/keyresult-detail.component';
import { of, switchMap } from 'rxjs';
import { OverviewComponent } from './components/overview/overview.component';
import { SidepanelComponent } from './shared/sidepanel/sidepanel.component';

const currentUserResolver: ResolveFn<User | undefined> = () => {
  const oauthService = inject(OidcSecurityService);
  const userService = inject(UserService);
  return oauthService.getUserData().pipe(
    switchMap((token) => {
      if (token) {
        return userService.getOrInitCurrentUser();
      }
      return of(undefined);
    }),
  );
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
        component: SidepanelComponent,
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
    canActivate: [AutoLoginPartialRoutesGuard],
  },
  { path: 'callback', component: CallbackComponent },
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
