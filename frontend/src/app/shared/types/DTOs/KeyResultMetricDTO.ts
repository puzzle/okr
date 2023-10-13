import { KeyResultDTO } from './KeyResultDTO';

export interface KeyResultMetricDTO extends KeyResultDTO {
  unit: string | null;
  baseline: number | null;
  stretchGoal: number | null;
}
