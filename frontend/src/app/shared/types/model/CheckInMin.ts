export interface CheckInMin {
  id: number;
  value: number | string | undefined;
  confidence: number;
  createdOn: Date;
  changeInfo: string;
  initiatives: string;
}
