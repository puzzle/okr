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
  if (!user || !user.userTeamList) {
    return [];
  }

  const test = user.userTeamList
    .map((u) => u.team)
    .filter((team) => isTeamArchived(team) || (new Date(team.markedAsArchivedAt!)
      ?.getTime() ?? 0) > (quarter?.startDate?.getTime() ?? 0));

  test.forEach((t) => console.log('user model ids returned: ' + t.id));
  return test;
};

export const getFullNameOfUser = (user: User | UserTableEntry) => {
  return `${user?.firstName || ''} ${user?.lastName || ''}`;
};

const isTeamArchived = (team: Team): boolean => !team.markedAsArchivedAt;

