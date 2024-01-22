import { KeyresultMin } from './KeyresultMin';
import { QuarterMin } from './QuarterMin';
import { State } from '../enums/State';

export interface ObjectiveMin {
  id: number;
  version: number;
  title: string;
  state: State;
  quarter: QuarterMin;
  keyResults: KeyresultMin[];
}
