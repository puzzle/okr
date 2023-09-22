import { Objective } from './Objective';
import { CheckIn } from './CheckIn';
import { User } from './User';

export interface KeyResult {
  id: number;
  keyResultType: string;
  title: string;
  description: string;

  // Datatype needs to be changed to user once it exists
  owner: User;
  objective: Objective;
  lastCheckIn: CheckIn | null;
  createdOn: Date;
  modifiedOn: Date;
}
