import { State } from '../enums/State';
import { User } from './User';

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
  writeable: boolean;
}
