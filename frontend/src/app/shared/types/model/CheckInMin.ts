export interface CheckInMin {
  id: number;
  valueMetric: number | undefined;
  zone: string | undefined;
  confidence: number;
  createdOn: Date;
  changeInfo: string;
  initiatives: string;
}
