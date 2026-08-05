import { CanActivateFn, ParamMap, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { forkJoin, map, Observable, switchMap } from 'rxjs';
import { QuarterService } from '../services/quarter.service';
import { UserService } from '../services/user.service';
import { TeamStateService } from '../services/team.state.service';
import { extractActiveTeamsFromUser } from '../shared/types/model/user';
import { Team } from '../shared/types/model/team';

interface RequestParams {
  quarterId: number | undefined;
  teamIds: number[] | undefined;
}

interface ResponseParams {
  quarterId: number;
  teamIds: number[] | undefined;
}

export const defaultQueryParamsGuard: CanActivateFn = (route, state: RouterStateSnapshot): Observable<boolean | UrlTree> => {
  const quarterService = inject(QuarterService);
  const teamStateService = inject(TeamStateService);
  const router = inject(Router);
  const userService = inject(UserService);

  const requestParams = parseParams(route.queryParamMap, !router.navigated);

  return forkJoin({ currentQuarter: quarterService.getCurrentQuarter(),
    user: userService.getOrInitCurrentUser() })
    .pipe(switchMap(({ currentQuarter, user }) => {
      const targetQuarterId = requestParams.quarterId ?? currentQuarter.id;

      return teamStateService.loadTeams({ quarterId: targetQuarterId })
        .pipe(map((teams) => teams.map((team) => team.id)), map((currentTeamIds) => {
          const redirectParams: ResponseParams = { quarterId: targetQuarterId,
            teamIds: requestParams.teamIds };

          if (requestParams.teamIds === undefined) {
            const userTeams = extractActiveTeamsFromUser(user);
            const availableRequestedTeamIds = filterActiveTeams(toTeamIds(userTeams), currentTeamIds);
            if (availableRequestedTeamIds.length > 0) {
              redirectParams.teamIds = availableRequestedTeamIds;
            }
          } else {
            const availableRequestedTeamIds = filterActiveTeams(requestParams.teamIds, currentTeamIds);
            if (!containsSameValues(availableRequestedTeamIds, requestParams.teamIds)) {
              redirectParams.teamIds = availableRequestedTeamIds;
            }
          }


          console.log(requestParams);
          console.log(redirectParams);
          console.log(requestParams.quarterId !== redirectParams.quarterId, !containsSameValues(requestParams.teamIds, redirectParams.teamIds));

          if (requestParams.quarterId !== redirectParams.quarterId || !containsSameValues(requestParams.teamIds, redirectParams.teamIds)) {
            const targetPath = state.url.split('?')[0];
            console.log('redirectParams', redirectParams);
            return router.createUrlTree([targetPath], {
              queryParams: { quarter: redirectParams.quarterId,
                teams: redirectParams.teamIds }
            });
          }
          return true;
        }));
    }));
};

const filterActiveTeams = (teamIds: number[], activeTeamIds: any) => teamIds.filter((id) => activeTeamIds.includes(id));

const toTeamIds = (teams: Team[]) => teams.map((team) => team.id);

const containsSameValues = (array1: number[] | undefined, array2: number[] | undefined) => array1?.every((value) => array2?.includes(value)) && array1.length === array2?.length;

const parseParams = (paramMap: ParamMap, initialLoad: boolean): RequestParams => {
  const quarterIdStr = paramMap.getAll('quarter')[0];
  const teamIdsStr = paramMap.getAll('teams');
  console.log('navigated÷ ', initialLoad, teamIdsStr);
  const quaterId = Number.parseInt(quarterIdStr);
  return {
    quarterId: Number.isSafeInteger(quaterId) ? quaterId : undefined,
    teamIds: initialLoad && teamIdsStr.length === 0 ? undefined : teamIdsStr.map((id: string) => Number.parseInt(id))
  };
};


