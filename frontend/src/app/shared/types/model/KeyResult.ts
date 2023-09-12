import { Objective } from './Objective';
import { CheckIn } from "./CheckIn";

export interface KeyResult {
  id: number;
  keyResultType: string;
  title: string;
  description: string;

  // Datatype needs to be changed to user once it exists
  owner: string;
  objective: Objective;
  lastCheckIn: CheckIn | null;
  createdOn: Date;
  modifiedOn: Date;
}
