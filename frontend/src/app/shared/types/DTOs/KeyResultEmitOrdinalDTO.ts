import {KeyResultEmitDTO} from "./KeyResultEmitDTO";

export interface KeyResultEmitOrdinalDTO extends KeyResultEmitDTO {
  commitZone: string | null | undefined;
  targetZone: string | null | undefined;
  stretchZone: string | null | undefined;
}
