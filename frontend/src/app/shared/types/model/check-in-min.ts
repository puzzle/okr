export interface CheckInMin {
  id?: number;
  version: number;
  confidence: number;
  createdBy?: string;
  createdOn: Date;
  changeInfo: string;
  initiatives: string;
  isWriteable: boolean;
}
