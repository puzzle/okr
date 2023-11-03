import { KeyResultObjective } from './KeyResultObjective';
import { CheckIn } from './CheckIn';
import { User } from './User';
import { Action } from './Action';

export interface KeyResult {
  id: number;
  version: number;
  keyResultType: string;
  title: string;
  description: string;

  owner: User;
  objective: KeyResultObjective;
  lastCheckIn: CheckIn | null;
  createdOn: Date;
  modifiedOn: Date;
  actionList: Action[] | null;
}
