import { CheckInMin } from './CheckInMin';

export interface KeyresultMin {
  id: number;
  title: string;
  keyResultType: string;
  lastCheckIn: CheckInMin | null;
}
