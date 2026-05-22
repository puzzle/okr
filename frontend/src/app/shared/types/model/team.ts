import { TeamStatus } from '../enums/team-status';

export interface Team {
  selectedTeam: import('/home/mmoeri/IdeaProjects/Puzzle-ITC/okr/frontend/src/app/shared/types/model/quarter').Quarter;
  id: number;
  version: number;
  name: string;
  description: string | null;
  isWriteable: boolean;
  markedAsArchivedAt: Date | null;
  status: TeamStatus;
}
