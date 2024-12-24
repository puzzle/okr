import { KeyResultMin } from './key-result-min';
import { QuarterMin } from './quarter-min';
import { State } from '../enums/state';

export interface ObjectiveMin {
  id: number;
  version: number;
  title: string;
  state: State;
  quarter: QuarterMin;
  keyResults: KeyResultMin[];
}
