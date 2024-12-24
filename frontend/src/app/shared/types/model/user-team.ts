import { Team } from './team';

export interface UserTeam {
  id?: number;
  team: Team;
  isTeamAdmin: boolean;
}
