import { AlignmentPossibilityObject } from './AlignmentPossibilityObject';

export interface AlignmentPossibility {
  teamId: number;
  teamName: string;
  alignmentObjectDtos: AlignmentPossibilityObject[];
}
