import { KeyResultMin } from './key-result-min';
import { CheckInOrdinalMin } from './check-in-ordinal-min';

export interface KeyResultOrdinalMin extends KeyResultMin {
  commitZone: string;
  targetZone: string;
  stretchGoal: string;
  lastCheckIn: CheckInOrdinalMin | null;
}
