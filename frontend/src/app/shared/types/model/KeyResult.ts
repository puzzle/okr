import { KeyResultObjective } from './KeyResultObjective';
import { CheckIn, CheckInMetric, CheckInOrdinal } from './CheckIn';
import { User } from './User';
import { Action } from './Action';
import { CheckInMin } from './CheckInMin';

export interface KeyResult {
  id: number;
  version: number;
  keyResultType: string;
  title: string;
  description: string;

  owner: User;
  objective: KeyResultObjective;
  // lastCheckIn<T extends CheckIn>(cls: { new (): T }): T | null;
  // lastCheckIn: CheckInMetric | CheckInOrdinal | null;
  createdOn: Date;
  modifiedOn: Date;
  actionList: Action[] | null;
  writeable: boolean;
}
