import { RowObject } from './RowObject';

export interface Objective extends RowObject {
  ownerId: number;
  quarterNumber: number;
  quarterYear: number;
  description: string;
}
