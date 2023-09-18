import { KeyresultMin } from './KeyresultMin';
import { QuarterMin } from './QuarterMin';
import { State } from '../enums/State';
import { state } from '@angular/animations';
import { quarter } from '../../testData';

//Class instead of interface since you cannot use instanceof on an interface
export class ObjectiveMin {
  id: number;
  title: string;
  state: State;
  quarter: QuarterMin;
  keyResults: KeyresultMin[];

  constructor(id: number, title: string, state: State, quarter: QuarterMin, keyResults: KeyresultMin[]) {
    this.id = id;
    this.title = title;
    this.state = state;
    this.quarter = quarter;
    this.keyResults = keyResults;
  }
}
