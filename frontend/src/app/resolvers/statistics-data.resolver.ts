import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { StatisticsService } from '../services/statistics.service';

export const statisticsDataResolver: ResolveFn<unknown> = (route) => {
  const statisticsService = inject(StatisticsService);
  return statisticsService.load(route.queryParams);
};
