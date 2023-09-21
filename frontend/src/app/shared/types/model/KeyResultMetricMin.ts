import { KeyresultMin } from './KeyresultMin';

export interface KeyResultMetricMin extends KeyresultMin {
  baseLine: number;
  stretchGoal: number;
  unit: string;
}
