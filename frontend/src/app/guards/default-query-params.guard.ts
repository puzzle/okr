import { CanActivateFn, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { forkJoin, Observable, map, switchMap } from 'rxjs';
import { QuarterService } from '../services/quarter.service';
import { UserService } from '../services/user.service';
import { TeamStateService } from '../services/team.state.service';
import { extractActiveTeamsFromUser } from '../shared/types/model/user';

export const defaultQueryParamsGuard: CanActivateFn = (route, state: RouterStateSnapshot): Observable<boolean | UrlTree> => {
  const router = inject(Router);
  const quarterService = inject(QuarterService);
  const userService = inject(UserService);
  const teamStateService = inject(TeamStateService);

  const quarterQuery = route.queryParams['quarter'];
  const teamsQuery = route.queryParams['teams'];

  return forkJoin({
    currentQuarter: quarterService.getCurrentQuarter(),
    userInit: userService.getOrInitCurrentUser()
  })
    .pipe(switchMap(({ currentQuarter }) => {
      const quarterIdStr = Array.isArray(quarterQuery) ? quarterQuery[0] : quarterQuery;
      const targetQuarterId = quarterIdStr && !isNaN(Number(quarterIdStr))
        ? parseInt(quarterIdStr, 10)
        : currentQuarter.id;

      return teamStateService.loadTeams({ quarterId: targetQuarterId })
        .pipe(map((activeTeams) => {
          const mergedParams: any = { ...route.queryParams };
          let needsRedirect = false;

          if (!quarterQuery || isNaN(Number(quarterIdStr))) {
            mergedParams.quarter = targetQuarterId;
            needsRedirect = true;
          }

          const activeTeamIds = activeTeams.map((t) => t.id);

          if (teamsQuery !== undefined) {
            if (teamsQuery !== '') {
              const urlTeamIds = teamsQuery.split(',')
                .map((id: string) => parseInt(id, 10));
              const validUrlTeamIds = urlTeamIds.filter((id: number) => activeTeamIds.includes(id));

              if (validUrlTeamIds.length !== urlTeamIds.length) {
                if (validUrlTeamIds.length > 0) {
                  mergedParams.teams = validUrlTeamIds.join(',');
                } else {
                  delete mergedParams.teams; // Remove key entirely instead of setting null
                }
                needsRedirect = true;
              }
            }
          } else if (!router.navigated) {
            const user = userService.getCurrentUser();
            const userTeams = extractActiveTeamsFromUser(user);

            const validUserTeamIds = userTeams
              .map((t) => t.id)
              .filter((id) => activeTeamIds.includes(id));

            if (validUserTeamIds.length > 0) {
              mergedParams.teams = validUserTeamIds.join(',');
              needsRedirect = true;
            }
          }

          if (needsRedirect) {
            const targetPath = state.url.split('?')[0];

            return router.createUrlTree([targetPath], {
              queryParams: mergedParams
            });
          }

          return true;
        }));
    }));
};
