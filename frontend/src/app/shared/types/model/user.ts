import { UserTeam } from './user-team';
import { UserTableEntry } from './user-table-entry';
import { Quarter } from './quarter';
import { Team } from './team';

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  userTeamList: UserTeam[];
  isOkrChampion: boolean;
}

export const extractActiveTeamsFromUser = (user: User, quarter?: Quarter) => {
  if (!user?.userTeamList) {
    return [];
  }

  return user.userTeamList
    .map((u) => u.team)
    .filter((team) => isTeamArchived(team) || isTeamArchivedAfterQuarterStarted(team, quarter));
};

export const getFullNameOfUser = (user: User | UserTableEntry) => {
  return `${user?.firstName || ''} ${user?.lastName || ''}`;
};

const isTeamArchived = (team: Team): boolean => !team.markedAsArchivedAt;

const isTeamArchivedAfterQuarterStarted = (team: Team, quarter: Quarter | undefined) => {
  const teamArchivedAt = new Date(team.markedAsArchivedAt!)
    ?.getTime() ?? 0;
  const quarterStartTime = quarter?.startDate?.getTime() ?? 0;

  return teamArchivedAt >= quarterStartTime;
};
