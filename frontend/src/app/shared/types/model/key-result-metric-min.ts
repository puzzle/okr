import { KeyResultMin } from './key-result-min';
import { CheckInMetricMin } from './check-in-metric-min';
import { Unit } from '../enums/unit';

export interface KeyResultMetricMin extends KeyResultMin {
  baseline: number;
  wordingCommitValue: number;
  wordingTargetValue: number;
  stretchGoal: number;
  unit: Unit;
  lastCheckIn: CheckInMetricMin | null;
}
