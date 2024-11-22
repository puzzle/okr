import { Team } from "./Team";

export interface UserTeam {
  id?: number;
  team: Team;
  isTeamAdmin: boolean;
}
