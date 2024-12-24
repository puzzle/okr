import { ObjectiveMin } from './objective-min';
import { TeamMin } from './team-min';

export interface OverviewEntity {
  team: TeamMin;
  objectives: ObjectiveMin[];
  isWriteable: boolean;
}
