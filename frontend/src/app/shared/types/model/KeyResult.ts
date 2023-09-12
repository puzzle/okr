import { CheckInMin } from './CheckInMin';
import { Objective } from './Objective';

export interface KeyResult {
  id: number;
  keyResultType: string;
  title: string;
  description: string;
  objective: Objective;
  lastCheckIn: CheckInMin | null;
  createdOn: Date;
  modifiedOn: Date;
}
