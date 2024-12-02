import { KeyresultMin } from './KeyresultMin';
import { CheckInMin, CheckInMinMetric } from './CheckInMin';

export interface KeyResultMetricMin extends KeyresultMin {
  baseline: number;
  stretchGoal: number;
  unit: string;
  // lastCheckIn<T extends CheckInMinMetric>(cls: { new (): T }): T | null;
  lastCheckIn: CheckInMinMetric | null;
}
