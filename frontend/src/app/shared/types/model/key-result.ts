import { KeyResultObjective } from './key-result-objective';
import { User } from './user';
import { Action } from './action';
import { CheckIn } from './check-in';

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
  lastCheckIn: CheckIn | null;
  isWriteable: boolean;
}
