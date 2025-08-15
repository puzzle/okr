import { KeyResult } from './key-result';
import { CheckInMetric } from './check-in-metric';
import { Unit } from '../enums/unit';

export interface KeyResultMetric extends KeyResult {
  lastCheckIn: CheckInMetric | null;
  baseline: number;
  commitValue: number;
  targetValue: number;
  stretchGoal: number;
  unit: Unit;
}
