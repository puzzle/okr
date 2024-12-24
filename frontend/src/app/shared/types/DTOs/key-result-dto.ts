import { User } from '../model/user';
import { Objective } from '../model/objective';
import { CheckIn } from '../model/check-in';
import { Action } from '../model/action';

export interface KeyResultDto {
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
