import { CheckInMin } from './check-in-min';
import { Zone } from '../enums/zone';

export interface CheckInOrdinalMin extends CheckInMin {
  zone: Zone;
}
