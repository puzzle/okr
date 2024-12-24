export interface Action {
  id: number | null;
  version: number;
  action: string;
  priority: number;
  isChecked: boolean;
  keyResultId: number | null;
}
