import { User } from "../model/User";
import { Objective } from "../model/Objective";
import { CheckIn } from "../model/CheckIn";

export interface KeyResultDTO {
  id: number | undefined;
  keyResultType: string | undefined;
  title: number;
  description: string;
  owner: User;
  objective: Objective;
  lastCheckIn: CheckIn | null | undefined;
  createdOn: Date | undefined;
  modifiedOn: Date | undefined;
}
