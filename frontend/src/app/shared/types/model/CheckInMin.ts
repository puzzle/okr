export interface CheckInMin {
  id?: number;
  version: number;
  confidence: number;
  createdOn: Date;
  changeInfo: string;
  initiatives: string;
  writeable: boolean;
}
export interface CheckInMinOrdinal extends CheckInMin {
  zone: string | undefined;
}
export interface CheckInMinMetric extends CheckInMin {
  value: number | undefined;
}
