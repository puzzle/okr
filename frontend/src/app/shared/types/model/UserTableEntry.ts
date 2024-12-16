import { User } from "./User";
import { UserRole } from "../enums/UserRole";
import { UserTeam } from "./UserTeam";

export interface UserTableEntry {
  id: number;
  firstname: string;
  lastname: string;
  email: string;
  teams: string[];
  roles: string[];
  isOkrChampion: boolean;
  userTeamList: UserTeam[];
}

export const convertFromUsers = (users: User[], teamId: number | null): UserTableEntry[] => {
  // make a deep copy to not modify original value
  const usersCopy = JSON.parse(JSON.stringify(users)) as User[];
  let userTableEntries;
  if (!teamId) {
    userTableEntries = usersCopy.map((u) => convertFromUser(u));
  } else {
    userTableEntries = usersCopy
      // first we filter user teams based on selected team id
      .map((u) => {
        u.userTeamList = u.userTeamList.filter((ut) => ut.team.id === teamId);
        return u;
      })
      // we remove users without membership in selected team id
      .filter((u) => u.userTeamList.length)
      .map((u) => convertFromUser(u));
  }
  return userTableEntries.sort((a, b) => a.firstname.localeCompare(b.firstname));
};

export const convertFromUser = (user: User): UserTableEntry => {
  const teams = user.userTeamList.map((ut) => ut.team.name);
  const roles = [];
  if (user.userTeamList.filter((ut) => ut.isTeamAdmin).length > 0) {
    roles.push(UserRole.TEAM_ADMIN);
  }
  if (user.userTeamList.filter((ut) => !ut.isTeamAdmin).length > 0) {
    roles.push(UserRole.TEAM_MEMBER);
  }

  return {
    id: user.id,
    firstname: user.firstname,
    lastname: user.lastname,
    email: user.email,
    teams,
    roles,
    isOkrChampion: user.isOkrChampion,
    userTeamList: user.userTeamList
  };
};
