import { UserTeam } from './user-team';
import { Team } from './team';

export class User {
  id: number;

  firstName: string;

  lastName: string;

  email: string;

  userTeamList: UserTeam[];

  isOkrChampion: boolean;

  constructor(
    id: number, firstName: string, lastName: string, email: string, userTeamList: UserTeam[], isOkrChampion: boolean
  ) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.userTeamList = userTeamList;
    this.isOkrChampion = isOkrChampion;
  }

  getFullName() {
    return `${this.firstName} ${this.lastName}`;
  }

  getTeamList(): Team[] {
    this.userTeamList.forEach((ut: UserTeam) => console.log('ut', ut));
    return [];
  }

  static getFullNameOfUser(user: User) {
    return user?.getFullName() || '';
  }
}
