import { CheckInMin } from './CheckInMin';
import {KeyResult} from "./KeyResult";

export interface KeyResultMetric extends KeyResult{
  id: number;
  title: string;
  lastCheckIn: CheckInMin | null;
}
