import { inject, NgModule } from '@angular/core';
import { ResolveFn, RouterModule, Routes } from '@angular/router';
import { OverviewComponent } from './components/overview/overview.component';
import { of } from 'rxjs';
import { SidePanelComponent } from './shared/side-panel/side-panel.component';
import { authGuard } from './guards/auth.guard';
import { UserService } from './services/user.service';
import { User } from './shared/types/model/user';
import { OAuthService } from 'angular-oauth2-oidc';
import { ObjectiveDetailComponent } from './components/objective-detail/objective-detail.component';
import { KeyResultDetailComponent } from './components/key-result-detail/key-result-detail.component';
import { TeamStateService } from './services/team.state.service';
import { defaultQueryParamsGuard } from './guards/default-query-params.guard';
import { overviewDataResolver } from './resolvers/overview-data.resolver';
import { statisticsDataResolver } from './resolvers/statistics-data.resolver';

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
    canActivate: [authGuard,
      defaultQueryParamsGuard],
    providers: [TeamStateService],
    children: [{
      // We duplicated the path, because we wanted to split the runGuardsAndResolvers value from resolver and guards
      canActivate: [defaultQueryParamsGuard],
      path: '',
      component: OverviewComponent,
      runGuardsAndResolvers: 'paramsOrQueryParamsChange',
      resolve: {
        user: currentUserResolver,
        filters: overviewDataResolver
      },
      children: [{
        path: 'details',
        component: SidePanelComponent,
        children: [{
          path: 'objective/:id',
          component: ObjectiveDetailComponent
        },
        {
          path: 'keyresult/:id',
          component: KeyResultDetailComponent
        }]
      }]
    }]
  },
  {
    path: 'team-management',
    loadChildren: () => import('./team-management/team-management.module').then((m) => m.TeamManagementModule),
    canActivate: [authGuard],
    resolve: { user: currentUserResolver }
  },
  { path: 'objective',
    redirectTo: 'details/objective' },
  { path: 'keyresult',
    redirectTo: 'details/keyresult' },
  {
    path: 'statistics',
    loadChildren: () => import('./statistics/statistics.module').then((m) => m.StatisticsModule),
    canActivate: [authGuard,
      defaultQueryParamsGuard],

    providers: [TeamStateService],
    resolve: { filters: statisticsDataResolver },

    runGuardsAndResolvers: 'paramsOrQueryParamsChange'
  },
  { path: '**',
    redirectTo: '',
    pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
