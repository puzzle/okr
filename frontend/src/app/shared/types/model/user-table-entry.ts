import { User } from './user';
import { UserRole } from '../enums/user-role';
import { UserTeam } from './user-team';

export class UserTableEntry extends User {
  teams: string[];

  roles: string[];

  constructor(
    id: number, firstName: string, lastName: string, email: string, userTeamList: UserTeam[], isOkrChampion: boolean, teams: string[], roles: string[]
  ) {
    super(
      id, firstName, lastName, email, userTeamList, isOkrChampion
    );
    this.teams = teams;
    this.roles = roles;
  }
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
  return userTableEntries.sort((a, b) => a.firstName.localeCompare(b.firstName));
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


  return new UserTableEntry(
    user.id, user.firstName, user.lastName, user.email, user.userTeamList, user.isOkrChampion, teams, roles
  );
};
