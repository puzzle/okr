import { CheckInMin } from './CheckInMin';
import { SidenavModel } from './SidenavModel';

export class KeyresultMin extends SidenavModel {
  title: string;
  keyResultType: string;
  lastCheckIn: CheckInMin | null;

  constructor(id: number, title: string, lastCheckIn: CheckInMin | null) {
    super(id, 'keyResult');
    this.id = id;
    this.title = title;
    this.lastCheckIn = lastCheckIn;
  }
}
