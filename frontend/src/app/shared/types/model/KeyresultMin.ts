import { CheckInMinMetric, CheckInMinOrdinal } from './CheckInMin';

export interface KeyresultMin {
  id: number;
  version: number;
  title: string;
  keyResultType: string;
  lastCheckIn: CheckInMinOrdinal | CheckInMinMetric | null;
}
