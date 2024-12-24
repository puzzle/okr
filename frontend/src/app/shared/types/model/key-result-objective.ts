import { State } from '../enums/state';
import { Quarter } from './quarter';

export interface KeyResultObjective {
  id: number;
  state: State;
  quarter: Quarter;
}
