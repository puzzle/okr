import { UserTeam } from './user-team';
import { UserTableEntry } from './user-table-entry';

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  userTeamList: UserTeam[];
  isOkrChampion: boolean;
}

export const extractTeamsFromUser = (user: User) => {
  return user.userTeamList.map((u) => u.team);
};

export const getFullNameOfUser = (user: User | UserTableEntry) => {
  return `${user.firstName} ${user.lastName}`;
};
