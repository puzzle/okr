import { User } from '../model/user';

export enum Unit {
  PERCENT = 'PERCENT',
  CHF = 'CHF',
  EUR = 'EUR',
  FTE = 'FTE',
  NUMBER = 'NUMBER'
}

export interface IUnit {
  id?: number;
  unitName: string;
  owner?: User;
}
