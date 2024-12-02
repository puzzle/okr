import { KeyresultMin } from './KeyresultMin';
import { CheckInMinMetric } from './CheckInMin';

export interface KeyResultMetricMin extends KeyresultMin {
  baseline: number;
  stretchGoal: number;
  unit: string;
  lastCheckIn: CheckInMinMetric | null;
}
