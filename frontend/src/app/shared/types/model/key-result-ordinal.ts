import { KeyResult } from './key-result';
import { CheckInOrdinal } from './check-in-ordinal';

export interface KeyResultOrdinal extends KeyResult {
  lastCheckIn: CheckInOrdinal | null;
  commitZone: string;
  targetZone: string;
  stretchZone: string;
}
