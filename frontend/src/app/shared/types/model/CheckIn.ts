export interface CheckIn {
  id: number;
  version: number;
  changeInfo: string;
  initiatives: string;
  confidence: number;
  createdBy: string;
  createdOn: Date;
  modifiedOn: Date;
  writeable: boolean;
}
