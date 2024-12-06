export interface CheckInMin {
  id?: number;
  version: number;
  confidence: number;
  createdOn: Date;
  changeInfo: string;
  initiatives: string;
  writeable: boolean;
}
