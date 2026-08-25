import { TeamStatus } from '../enums/team-status';

export interface Team {
  id: number;
  version: number;
  name: string;
  description: string | null;
  isWriteable: boolean;
  markedAsArchivedAt: Date | null;
  status: TeamStatus;
}
