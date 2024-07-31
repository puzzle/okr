export interface UserOkrData {
  keyResults: UserKeyResultData[];
}

export interface UserKeyResultData {
  keyResultId: number;
  keyResultName: string;
  objectiveId: number;
  objectiveName: string;
}
