import { KeyResult } from './KeyResult';

export interface CheckIn {
  id: number;
  changeInfo: string;
  initiatives: string;
  confidence: number;
  createdBy: string;
  createdOn: Date;
  modifiedOn: Date;
  value: string | number;
}
