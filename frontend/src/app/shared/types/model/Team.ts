import { Organisation } from './Organisation';

export interface Team {
  id: number;
  version: number;
  name: string;
  organisations: Organisation[];
  filterIsActive: boolean;
}
