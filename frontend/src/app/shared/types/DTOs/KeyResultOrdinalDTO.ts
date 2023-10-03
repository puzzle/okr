import { KeyResultDTO } from './KeyResultDTO';

export interface KeyResultOrdinalDTO extends KeyResultDTO {
  commitZone: string | undefined;
  targetZone: string | undefined;
  stretchZone: string | undefined;
}
