import { State } from '../enums/State';
import { Quarter } from './Quarter';

export interface KeyResultObjective {
  id: number;
  state: State;
  quarter: Quarter;
}
