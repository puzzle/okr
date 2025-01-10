import { UserTeam } from './user-team';

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

  get fullName() {
    return `${this.firstName} ${this.lastName}`;
  }

  get teamList() {
    return this.userTeamList.map((ut) => ut.team);
  }

  static getFullNameOfUser(user: User) {
    return user.fullName;
  }
}
