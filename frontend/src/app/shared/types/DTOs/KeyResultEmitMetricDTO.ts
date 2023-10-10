import {KeyResultEmitDTO} from "./KeyResultEmitDTO";

export interface KeyResultEmitMetricDTO extends KeyResultEmitDTO {
  baseline: number | null | undefined;
  stretchGoal: number | null | undefined;
  unit: string | null | undefined;
}
