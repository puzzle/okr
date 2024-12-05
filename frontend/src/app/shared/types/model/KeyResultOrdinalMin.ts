import { KeyresultMin } from './KeyresultMin';
import { CheckInOrdinalMin } from './CheckInOrdinalMin';

export interface KeyResultOrdinalMin extends KeyresultMin {
  commitZone: string;
  targetZone: string;
  stretchGoal: string;
  lastCheckIn: CheckInOrdinalMin | null;
}
