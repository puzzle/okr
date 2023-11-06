export interface CheckInMin {
  id?: number;
  version: number;
  value: number | string | undefined;
  confidence: number;
  createdOn: Date;
  changeInfo: string;
  initiatives: string;
  writeable: boolean;
}
