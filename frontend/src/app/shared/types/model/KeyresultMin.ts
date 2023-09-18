import { CheckInMin } from './CheckInMin';

//Class instead of interface since you cannot use instanceof on an interface
export class KeyresultMin {
  id: number;
  title: string;
  lastCheckIn: CheckInMin | null;

  constructor(id: number, title: string, lastCheckIn: CheckInMin | null) {
    this.id = id;
    this.title = title;
    this.lastCheckIn = lastCheckIn;
  }
}
