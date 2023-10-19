import { Objective } from './Objective';

export interface Completed {
  id: number | null;
  objective: Objective;
  comment: string | null;
}
