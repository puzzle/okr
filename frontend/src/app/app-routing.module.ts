import { NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, ResolveFn, RouterModule, Routes } from '@angular/router';
import { OverviewComponent } from './overview/overview.component';
import { EMPTY, of } from 'rxjs';
import { SidepanelComponent } from './shared/custom/sidepanel/sidepanel.component';
import { authGuard } from './shared/guards/auth.guard';

/**
 * Resolver for get the id from url like `/objective/42` or `/keyresult/42`.
 */
export const getIdFromPathResolver: ResolveFn<number> = (route: ActivatedRouteSnapshot) => {
  try {
    let id = Number.parseInt(route.url[1].path);
    return of(id);
  } catch (error) {
    console.error('Can not get id from URL:', error);
    return EMPTY;
  }
};

const routes: Routes = [
  {
    path: '',
    component: OverviewComponent,
    children: [
      {
        path: 'objective/:id',
        component: SidepanelComponent,
        resolve: { id: getIdFromPathResolver },
        data: { type: 'Objective' },
      },
      {
        path: 'keyresult/:id',
        component: SidepanelComponent,
        resolve: { id: getIdFromPathResolver },
        data: { type: 'KeyResult' },
      },
    ],
    canActivate: [authGuard],
  },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
