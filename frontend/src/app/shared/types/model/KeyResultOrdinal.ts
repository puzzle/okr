import { KeyResult } from './KeyResult';
import { CheckInOrdinal } from './CheckInOrdinal';

export interface KeyResultOrdinal extends KeyResult {
  lastCheckIn: CheckInOrdinal | null;
  commitZone: string;
  targetZone: string;
  stretchZone: string;
}
