import { KeyResultDto } from './key-result-dto';

export interface KeyResultMetricDto extends KeyResultDto {
  unit: string | null;
  baseline: number | null;
  wordingCommitValue: number | null;
  wordingTargetValue: number | null;
  stretchGoal: number | null;
}
