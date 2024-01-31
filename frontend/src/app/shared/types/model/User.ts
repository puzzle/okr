import { UserTeam } from './UserTeam';
import { UserTableEntry } from './UserTableEntry';

export interface User {
  id: number;
  firstname: string;
  lastname: string;
  email: string;
  userTeamList: UserTeam[];
  isOkrChampion: boolean;
}

export const extractTeamsFromUser = (user: User) => {
  return user.userTeamList.map((u) => u.team);
};

export const getFullNameFromUser = (user: User | UserTableEntry) => {
  return `${user.firstname} ${user.lastname}`;
};
