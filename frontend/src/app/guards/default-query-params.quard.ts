import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { forkJoin, Observable, map, of } from 'rxjs';
import { QuarterService } from '../services/quarter.service';
import { UserService } from '../services/user.service';
import { extractTeamsFromUser } from '../shared/types/model/user';

export const defaultQueryParamsGuard: CanActivateFn = (route): Observable<boolean | UrlTree> => {
  const router = inject(Router);
  const quarterService = inject(QuarterService);
  const userService = inject(UserService);

  const quarterQuery = route.queryParams['quarter'];
  const teamsQuery = route.queryParams['teams'];

  if (quarterQuery) {
    return of(true);
  }

  return forkJoin([quarterService.getAllQuarters(),
    quarterService.getCurrentQuarter(),
    userService.getOrInitCurrentUser()])
    .pipe(map(([allQuarters,
      currentQuarter]) => {
      const mergedParams: any = {};

      if (!quarterQuery) {
        mergedParams.quarter = currentQuarter.id;
      }

      if (!teamsQuery) {
        const userTeams = extractTeamsFromUser(userService.getCurrentUser());

        if (userTeams.length > 0) {
          mergedParams.teams = userTeams.map((t) => t.id)
            .join(',');
        } else {
          mergedParams.teams = null;
        }
      }

      return router.createUrlTree([], {
        queryParams: mergedParams,
        queryParamsHandling: 'merge'
      });
    }));
};
