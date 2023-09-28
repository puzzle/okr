import { State } from '../enums/State';

export interface Objective {
  id: number;
  title: string;
  description: string;
  state: State;
  teamId: number;
  quarterId: number;
  createdOn: Date;
  modifiedOn: Date;
}
