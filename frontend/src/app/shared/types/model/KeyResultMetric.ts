import { KeyResult } from './KeyResult';
import { CheckInMetric } from './CheckIn';
import { CheckInMin } from './CheckInMin';

export interface KeyResultMetric extends KeyResult {
  // lastCheckIn<T extends CheckInMetric>(cls: { new (): T }): T | null;
  lastCheckIn: CheckInMetric | null;
  baseline: number;
  stretchGoal: number;
  unit: string;
}
