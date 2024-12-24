import { KeyResultObjective } from './key-result-objective';
import { User } from './user';
import { Action } from './action';

export interface KeyResult {
  id: number;
  version: number;
  keyResultType: string;
  title: string;
  description: string;

  owner: User;
  objective: KeyResultObjective;
  createdOn: Date;
  modifiedOn: Date;
  actionList: Action[] | null;
  isWriteable: boolean;
}
