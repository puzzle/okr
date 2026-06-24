import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { TeamStateService } from '../services/team.state.service';

export const teamFilterResolver: ResolveFn<void> = (route) => {
  const teamStateService = inject(TeamStateService);
  const quarterIdParam = route.queryParams['quarterId'];

  const filters = quarterIdParam ? { quarterId: parseInt(quarterIdParam, 10) } : {};
  teamStateService.loadTeams(filters);
};
