import { KeyResult } from './KeyResult';

export interface Action {
  id: number | null;
  action: string;
  priority: number;
  isChecked: boolean;
}
