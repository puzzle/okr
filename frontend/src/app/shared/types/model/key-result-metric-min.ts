import { KeyResultMin } from './key-result-min';
import { CheckInMetricMin } from './check-in-metric-min';
import { Unit } from '../enums/unit';

export interface KeyResultMetricMin extends KeyResultMin {
  baseline: number;
  commitValue: number;
  targetValue: number;
  stretchGoal: number;
  unit: Unit;
  lastCheckIn: CheckInMetricMin | null;
}
