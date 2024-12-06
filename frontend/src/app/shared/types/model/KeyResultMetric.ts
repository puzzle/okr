import { KeyResult } from './KeyResult';
import { CheckInMetric } from './CheckInMetric';

export interface KeyResultMetric extends KeyResult {
  lastCheckIn: CheckInMetric | null;
  baseline: number;
  stretchGoal: number;
  unit: string;
}
