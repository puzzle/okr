import { KeyResult } from './KeyResult';

export interface KeyResultMetric extends KeyResult {
  baseline: number;
  stretchGoal: number;
  unit: string;
}
