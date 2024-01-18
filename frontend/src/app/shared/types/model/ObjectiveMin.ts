import { KeyresultMin } from './KeyresultMin';
import { State } from '../enums/State';
import { Quarter } from './Quarter';

export interface ObjectiveMin {
  id: number;
  version: number;
  title: string;
  state: State;
  quarter: Quarter;
  keyResults: KeyresultMin[];
}
