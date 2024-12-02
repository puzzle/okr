import { KeyResult } from './KeyResult';
import { CheckInOrdinal } from './CheckIn';
import { CheckInMin } from './CheckInMin';

export interface KeyResultOrdinal extends KeyResult {
  // lastCheckIn<T extends CheckInOrdinal>(cls: { new (): T }): T | null;
  lastCheckIn: CheckInOrdinal | null;
  commitZone: string;
  targetZone: string;
  stretchZone: string;
}
