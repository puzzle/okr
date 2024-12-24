import { KeyResultMin } from './KeyResultMin';
import { CheckInOrdinalMin } from './CheckInOrdinalMin';

export interface KeyResultOrdinalMin extends KeyResultMin {
  commitZone: string;
  targetZone: string;
  stretchGoal: string;
  lastCheckIn: CheckInOrdinalMin | null;
}
