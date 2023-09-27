import { User } from '../model/User';

export interface ObjectiveDTO {
  id: number | undefined;
  title: string;
  description: string;
  teamId: number;
  quarterId: number;
  state: 'DRAFT' | 'ONGOING' | 'SUCCESSFUL' | 'NOTSUCCESSFUL';
  createdOn: Date | undefined;
  modifiedOn: Date | undefined;
  createdBy: User | undefined;
}
