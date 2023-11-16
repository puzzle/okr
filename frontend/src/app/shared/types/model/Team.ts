import { Organisation } from './Organisation';

export interface Team {
  id: number;
  name: string;
  activeObjectives: number;
  organisations: Organisation[];
}
