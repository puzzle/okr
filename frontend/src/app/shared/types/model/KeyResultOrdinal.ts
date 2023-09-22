import { KeyResult } from './KeyResult';

export interface KeyResultOrdinal extends KeyResult {
  commitZone: string;
  targetZone: string;
  stretchZone: string;
}
