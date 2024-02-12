export interface AlignmentPossibility {
  objectiveId: number | null;
  objectiveTitle: string;
  keyResultAlignmentsDtos: {
    keyResultId: number;
    keyResultTitle: string;
  }[];
}
