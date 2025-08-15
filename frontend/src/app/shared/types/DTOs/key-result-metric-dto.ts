import { KeyResultDto } from './key-result-dto';

export interface KeyResultMetricDto extends KeyResultDto {
  unit: string | null;
  baseline: number | null;
  commitValue: number | null;
  targetValue: number | null;
  stretchGoal: number | null;
}
