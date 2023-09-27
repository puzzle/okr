import { User } from '../model/User';

export interface ObjectiveDTO {
  id: number | undefined;
  title: number;
  teamId: number;
  quarterId: number;
  description: string;
  state: 'DRAFT' | 'ONGOING' | 'SUCCESSFUL' | 'NOTSUCCESSFUL';
  createdOn: Date | undefined;
  modifiedOn: Date | undefined;
  createdBy: User | undefined;
}
