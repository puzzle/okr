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
export interface CheckInOrdinal extends CheckIn {
  zone: string;
}

export interface CheckInMetric extends CheckIn {
  value: number;
}
