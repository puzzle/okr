import { CanActivateFn, ParamMap, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { forkJoin, map, Observable, switchMap } from 'rxjs';
import { QuarterService } from '../services/quarter.service';
import { UserService } from '../services/user.service';
import { TeamStateService } from '../services/team.state.service';
import { extractActiveTeamsFromUser, User } from '../shared/types/model/user';
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

  const requestParams = parseParams(route.queryParamMap, router.navigated);

  return forkJoin({
    currentQuarter: quarterService.getCurrentQuarter(),
    user: userService.getOrInitCurrentUser()
  })
    .pipe(switchMap(({ currentQuarter, user }) => {
      const targetQuarterId = requestParams.quarterId ?? currentQuarter.id;

      return teamStateService.loadTeams({ quarterId: targetQuarterId })
        .pipe(map((teams) => teams.map((team) => team.id)), map((currentTeamIds) => {
          const targetTeamIds = resolveTeamIds(requestParams.teamIds, currentTeamIds, user);

          const redirectParams: ResponseParams = { quarterId: targetQuarterId,
            teamIds: targetTeamIds };

          if (areParamsDifferent(requestParams, redirectParams)) {
            const urlTree = router.parseUrl(state.url);

            urlTree.queryParams = {
              quarter: redirectParams.quarterId,
              teams: redirectParams.teamIds?.join(',')
            };

            return urlTree;
          }
          return true;
        }));
    }));
};

const resolveTeamIds = (teamIds: number[] | undefined, currentTeamIds: number[], user: User): number[] | undefined => {
  if (teamIds === undefined) {
    const userTeams = extractActiveTeamsFromUser(user);
    const availableRequestedTeamIds = filterActiveTeams(toTeamIds(userTeams), currentTeamIds);
    if (availableRequestedTeamIds.length > 0) {
      return availableRequestedTeamIds;
    }
  } else {
    const availableRequestedTeamIds = filterActiveTeams(teamIds, currentTeamIds);
    if (!containsSameValues(availableRequestedTeamIds, teamIds)) {
      return availableRequestedTeamIds;
    }
  }

  return teamIds;
};

const filterActiveTeams = (teamIds: number[], activeTeamIds: any) => teamIds.filter((id) => activeTeamIds.includes(id));

const toTeamIds = (teams: Team[]) => teams.map((team) => team.id);

const containsSameValues = (array1: number[] | undefined, array2: number[] | undefined): boolean => {
  if (array1 === array2) {
    return true;
  }

  if (!array1 || !array2) {
    return false;
  }

  return array1.every((value) => array2.includes(value)) && array1.length === array2.length;
};
const parseParams = (paramMap: ParamMap, userAlreadyOnPage: boolean): RequestParams => {
  const quarterIdStr = paramMap.getAll('quarter')[0];
  const quaterId = Number.parseInt(quarterIdStr);
  const validQuaterId = Number.isSafeInteger(quaterId) ? quaterId : undefined;

  const teamIdsStr = normalizeParamList(paramMap.getAll('teams'));

  const isInitialLoad = !userAlreadyOnPage;
  const isTeamEmpty = teamIdsStr?.length === 0;
  const ignoreEmptyTeams = isInitialLoad && isTeamEmpty;

  const parsedTeamIds = ignoreEmptyTeams ? undefined : teamIdsStr?.map((id: string) => Number.parseInt(id));

  return {
    quarterId: validQuaterId,
    teamIds: parsedTeamIds
  };
};

const normalizeParamList = (list: string[]) => list.flatMap((v) => v.split(','))
  .map((v) => v.trim())
  .filter((v) => v.length > 0);

const areParamsDifferent = (requestParams: RequestParams, redirectParams: ResponseParams) => requestParams.quarterId !== redirectParams.quarterId || !containsSameValues(requestParams.teamIds, redirectParams.teamIds);
