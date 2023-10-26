import { User } from '../model/User';
import { Objective } from '../model/Objective';
import { CheckIn } from '../model/CheckIn';
import { Action } from '../model/Action';

export interface KeyResultDTO {
  id: number | undefined;
  version: number;
  keyResultType: string | undefined;
  title: string;
  description: string;
  owner: User;
  objective: Objective;
  lastCheckIn: CheckIn | null | undefined;
  createdOn: Date | null | undefined;
  modifiedOn: Date | null | undefined;
  actionList: Action[] | null;
}
