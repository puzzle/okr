import { UserTeam } from './UserTeam';

export interface User {
  id: number;
  firstname: string;
  lastname: string;
  email: string;
  userTeamList: UserTeam[];
  isOkrChampion: boolean;
  isWriteable: boolean;
}

export const extractTeamsFromUser = (user: User) => {
  return user.userTeamList.map((u) => u.team);
};

export const getFullNameFromUser = (user: User) => {
  return `${user.firstname} ${user.lastname}`;
};
