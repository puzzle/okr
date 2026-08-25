import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { OverviewService } from '../services/overview.service';

export const overviewDataResolver: ResolveFn<unknown> = (route) => {
  const overviewService = inject(OverviewService);
  return overviewService.load(route.queryParams);
};
