import { KeyResultMin } from './KeyResultMin';
import { CheckInMetricMin } from './CheckInMetricMin';

export interface KeyResultMetricMin extends KeyResultMin {
  baseline: number;
  stretchGoal: number;
  unit: string;
  lastCheckIn: CheckInMetricMin | null;
}
