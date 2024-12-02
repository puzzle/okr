import { KeyresultMin } from './KeyresultMin';
import { CheckInMinOrdinal } from './CheckInMin';

export interface KeyResultOrdinalMin extends KeyresultMin {
  commitZone: string;
  targetZone: string;
  stretchGoal: string;
  // lastCheckIn<T extends CheckInMinOrdinal>(cls: { new (): T }): T | null;
  lastCheckIn: CheckInMinOrdinal | null;
}
