import { KeyresultMin } from './KeyresultMin';
import { CheckInMetricMin } from './CheckInMetricMin';

export interface KeyResultMetricMin extends KeyresultMin {
  baseline: number;
  stretchGoal: number;
  unit: string;
  lastCheckIn: CheckInMetricMin | null;
}
