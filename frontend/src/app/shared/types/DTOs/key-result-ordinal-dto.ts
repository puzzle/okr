import { KeyResultDto } from './key-result-dto';

export interface KeyResultOrdinalDto extends KeyResultDto {
  commitZone: string | undefined;
  targetZone: string | undefined;
  stretchZone: string | undefined;
}
