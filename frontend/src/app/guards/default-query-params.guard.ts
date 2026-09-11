import { CanActivateFn, ParamMap, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { filter, forkJoin, map, Observable, switchMap, take, timer } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';
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

const TOKEN_POLL_INTERVAL_MS = 50;
const TOKEN_MAX_WAIT_MS = 2000;

export const defaultQueryParamsGuard: CanActivateFn = (route, state: RouterStateSnapshot): Observable<boolean | UrlTree> => {
  const quarterService = inject(QuarterService);
  const teamStateService = inject(TeamStateService);
  const oAuthService = inject(OAuthService);
  const router = inject(Router);
  const userService = inject(UserService);

  const requestParams = parseParams(route.queryParamMap, router.navigated);

  return waitForAccessToken(oAuthService)
    .pipe(switchMap(() => forkJoin({
      currentQuarter: quarterService.getCurrentQuarter(),
      availableQuarters: quarterService.getAllQuarters(),
      user: userService.getOrInitCurrentUser()
    })))
    .pipe(switchMap(({ currentQuarter, availableQuarters, user }) => {
      const targetQuarterId = availableQuarters.some((q) => q.id === requestParams.quarterId)
        ? requestParams.quarterId as number
        : currentQuarter.id;

      return teamStateService.loadTeams({ quarterId: targetQuarterId })
        .pipe(map((teams) => teams.map((team) => team.id)), map((currentTeamIds) => {
          const targetTeamIds = resolveTeamIds(requestParams.teamIds, currentTeamIds, user);

          const redirectParams: ResponseParams = { quarterId: targetQuarterId,
            teamIds: targetTeamIds };

          if (areParamsDifferent(requestParams, redirectParams)) {
            const urlTree = router.parseUrl(state.url);

            urlTree.queryParams = normalizeRedirectParams(redirectParams);

            return urlTree;
          }
          return true;
        }));
    }));
};

const waitForAccessToken = (oAuthService: OAuthService): Observable<void> => timer(0, TOKEN_POLL_INTERVAL_MS)
  .pipe(
    map((tick) => ({ tick,
      ready: oAuthService.hasValidAccessToken() })), filter(({ tick, ready }) => ready || tick * TOKEN_POLL_INTERVAL_MS >= TOKEN_MAX_WAIT_MS), take(1), map(() => undefined)
  );

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

const normalizeRedirectParams = (redirectParams: ResponseParams) => {
  const normalizedParams: any = {
    quarter: redirectParams.quarterId.toString()
  };

  if (redirectParams.teamIds !== undefined) {
    normalizedParams.teams = redirectParams.teamIds.join(',');
  }

  return normalizedParams;
};
