import { Objective } from './objective';

export interface Completed {
  id: number | null;
  version: number;
  objective: Objective;
  comment: string | null;
}
