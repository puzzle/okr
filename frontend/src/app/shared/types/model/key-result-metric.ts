import { KeyResult } from './key-result';
import { CheckInMetric } from './check-in-metric';

export interface KeyResultMetric extends KeyResult {
  lastCheckIn: CheckInMetric | null;
  baseline: number;
  stretchGoal: number;
  unit: string;
}
