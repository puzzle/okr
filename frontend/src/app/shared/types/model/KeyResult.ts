import { Objective } from './Objective';
import { CheckIn } from './CheckIn';
import { User } from './User';

export interface KeyResult {
  id: number;
  keyResultType: string;
  title: string;
  description: string;

  owner: User;
  objective: Objective;
  lastCheckIn: CheckIn | null;
  createdOn: Date;
  modifiedOn: Date;
}
