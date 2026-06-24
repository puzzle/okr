import { InjectionToken } from '@angular/core';
import { TeamStateService } from './team.state.service';

export const ALL_TEAMS_STATE = new InjectionToken<TeamStateService>('AllTeamsState', {
  providedIn: 'root',
  factory: () => {
    const service = new TeamStateService();

    service.loadTeams();

    return service;
  }
});
