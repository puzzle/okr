import { InjectionToken } from '@angular/core';
import { TeamStateService } from './team.state.service';

export const ALL_TEAMS_STATE = new InjectionToken<TeamStateService>('AllTeamsState', {
  providedIn: 'root',
  factory: () => new TeamStateService()
});

export const FILTERED_TEAMS_STATE = new InjectionToken<TeamStateService>('FilteredTeamsState', {
  providedIn: 'root',
  factory: () => new TeamStateService()
});
