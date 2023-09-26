import { State } from '../enums/State';
import { TeamMin } from './TeamMin';
import { Quarter } from './Quarter';

export interface KeyResultObjective {
  id: number;
  title: string;
  description: string;
  state: State;
  team: TeamMin;
  quarter: Quarter;
  createdOn: Date;
  modifiedOn: Date;
}
