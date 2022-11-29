import { RowObject } from './RowObject';

export interface Objective extends RowObject {
  ownerId: number;
  ownerFirstname: string;
  ownerLastname: string;
  quarterId: number;
  quarterNumber: number;
  quarterYear: number;
  description: string;
}
