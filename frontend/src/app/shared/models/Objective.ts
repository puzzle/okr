import { State } from '../types/enums/State';

export interface Objective {
  id: number;
  title: string;
  state: State;
}
