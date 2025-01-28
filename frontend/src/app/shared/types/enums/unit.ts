import { User } from '../model/user';

export interface Unit {
  id?: number;
  unitName: string;
  owner?: User;
  isDefault: boolean;
}
