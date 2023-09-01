import { KeyresultMin } from './KeyresultMin';
import { QuarterMin } from './QuarterMin';
import { State } from '../shared/types/enums/State';

export interface ObjectiveMin {
  id: number;
  title: string;
  state: State;
  quarter: QuarterMin;
  keyresults: KeyresultMin[];
}
