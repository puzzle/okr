import { KeyResult } from './KeyResult';

export interface Action {
  id: number;
  action: string;
  priority: number;
  isChecked: boolean;
  keyResult: KeyResult;
}
