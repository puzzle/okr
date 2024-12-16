import { KeyResultObjective } from "./KeyResultObjective";
import { User } from "./User";
import { Action } from "./Action";

export interface KeyResult {
  id: number;
  version: number;
  keyResultType: string;
  title: string;
  description: string;

  owner: User;
  objective: KeyResultObjective;
  createdOn: Date;
  modifiedOn: Date;
  actionList: Action[] | null;
  writeable: boolean;
}
