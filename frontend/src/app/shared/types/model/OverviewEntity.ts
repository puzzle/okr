import { ObjectiveMin } from './ObjectiveMin';
import { TeamMin } from './TeamMin';

export interface OverviewEntity {
  team: TeamMin;
  objectives: ObjectiveMin[];
  isWriteable: boolean;
}
