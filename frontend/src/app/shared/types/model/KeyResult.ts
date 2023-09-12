import { CheckInMin } from './CheckInMin';

export interface KeyResult {
  id: number;
  title: string;
  description: string;
  lastCheckIn: CheckInMin | null;

}
