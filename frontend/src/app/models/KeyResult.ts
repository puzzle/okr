import { RowObject } from './RowObject';

export interface KeyResult extends RowObject {
  details: string;
  lastMeasure: string; //Change to object
}
