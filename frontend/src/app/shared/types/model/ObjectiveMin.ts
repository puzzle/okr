import { KeyresultMin } from './KeyresultMin';
import { QuarterMin } from './QuarterMin';
import { State } from '../enums/State';
import { SidenavModel } from './SidenavModel';
export class ObjectiveMin extends SidenavModel {
  title: string;
  state: State;
  quarter: QuarterMin;
  keyResults: KeyresultMin[];

  constructor(id: number, title: string, state: State, quarter: QuarterMin, keyResults: KeyresultMin[]) {
    super(id, 'objective');
    this.title = title;
    this.state = state;
    this.quarter = quarter;
    this.keyResults = keyResults;
  }
}
