import { KeyResultMin } from './key-result-min';
import { CheckInMetricMin } from './check-in-metric-min';

export interface KeyResultMetricMin extends KeyResultMin {
  baseline: number;
  stretchGoal: number;
  unit: string;
  lastCheckIn: CheckInMetricMin | null;
}
