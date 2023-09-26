import { KeyResultDTO } from "./KeyResultDTO";

export interface KeyResultMetricDTO extends KeyResultDTO {
  unit: string | undefined;
  baseline: number | undefined;
  stretchGoal: number | undefined;
}
