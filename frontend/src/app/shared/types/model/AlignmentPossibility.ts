import { AlignmentPossibilityObject } from './AlignmentPossibilityObject';

export interface AlignmentPossibility {
  teamId: number;
  teamName: string;
  alignmentObjects: AlignmentPossibilityObject[];
}
