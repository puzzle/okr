import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { TeamStateService } from '../services/team.state.service';
import { Team } from '../shared/types/model/team';

export const teamFilterResolver: ResolveFn<Team[]> = (route) => {
  const teamStateService = inject(TeamStateService);

  const quarterQuery = route.queryParams['quarter'];
  const quarterIdStr = Array.isArray(quarterQuery) ? quarterQuery[0] : quarterQuery;

  const filters = quarterIdStr && !isNaN(parseInt(quarterIdStr))
    ? { quarterId: parseInt(quarterIdStr) }
    : {};

  return teamStateService.loadTeams(filters);
};
