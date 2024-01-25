import { ObjectiveMin } from './ObjectiveMin';
import { Team } from './Team';

export interface OverviewEntity {
  team: Team;
  objectives: ObjectiveMin[];
  writable: boolean;
}
