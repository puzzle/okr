import { CheckInMin } from './CheckInMin';
import {KeyResult} from "./KeyResult";

export interface KeyResultOrdinal extends KeyResult{
  id: number;
  title: string;
  lastCheckIn: CheckInMin | null;
}
