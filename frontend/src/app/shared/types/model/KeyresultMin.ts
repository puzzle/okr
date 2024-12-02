import { CheckInMin, CheckInMinMetric, CheckInMinOrdinal } from './CheckInMin';

export interface KeyresultMin {
  id: number;
  version: number;
  title: string;
  keyResultType: string;
  // lastCheckIn<T extends CheckInMin>(cls: { new (): T }): T | null;
}
