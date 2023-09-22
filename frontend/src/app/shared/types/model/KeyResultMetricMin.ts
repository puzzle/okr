import { KeyresultMin } from './KeyresultMin';

export interface KeyResultMetricMin extends KeyresultMin {
  baseline: number;
  stretchGoal: number;
  unit: string;
}
