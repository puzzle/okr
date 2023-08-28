import { Quarter } from '../release-1/shared/services/quarter.service';
import { KeyresultMin } from './KeyresultMin';

export interface ObjectiveMin {
  id: number;
  title: string;
  state: string;
  quarter: Quarter;
  keyresults: KeyresultMin[];
}
