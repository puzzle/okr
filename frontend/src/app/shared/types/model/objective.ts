import { State } from '../enums/state';
import { User } from './user';

export interface Objective {
  id: number;
  version: number;
  title: string;
  description: string;
  state: State;
  teamId: number;
  quarterId: number;
  createdOn?: Date;
  modifiedOn?: Date;
  createdBy?: User;
  isWriteable: boolean;
}
