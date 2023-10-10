import {User} from "../model/User";

export interface KeyResultEmitDTO {
  keyresultType: string;
  owner: User | null | undefined;
}
