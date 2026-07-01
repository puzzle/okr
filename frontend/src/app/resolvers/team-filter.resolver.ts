import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { TeamStateService } from '../services/team.state.service';

export const teamFilterResolver: ResolveFn<void> = (route) => {
  const teamStateService = inject(TeamStateService);

  const quarterQuery = route.queryParams['quarter'];
  const quarterIdStr = Array.isArray(quarterQuery) ? quarterQuery[0] : quarterQuery;

  const filters = quarterIdStr && !isNaN(Number(quarterIdStr))
    ? { quarterId: parseInt(quarterIdStr, 10) }
    : {};

  teamStateService.loadTeams(filters);
};
