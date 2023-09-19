import { KeyResult } from './KeyResult';

export interface CheckIn {
  id: number;
  changeInfo: string;
  initiatives: string;
  confidence: number;
  keyresult: KeyResult;
  createdBy: string;
  createdOn: Date;
  modifiedOn: Date;
}
