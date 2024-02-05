export interface Alignment {
  alignmentId: number;
  alignedObjectiveId: number;
  alignedObjectiveTitle: string;
  alignedObjectiveTeamId: number;
  alignedObjectiveTeamName: string;
  alignedObjectiveQuarterId: number;
  alignmentType: string;
  targetObjectiveId: number | undefined;
  targetObjectiveTitle: string | undefined;
  targetObjectiveTeamName: string | undefined;
  targetKeyResultId: number | undefined;
  targetKeyResultTitle: string | undefined;
  targetKeyResultTeamName: string | undefined;
}
