export interface Team {
  id: number;
  version: number;
  name: string;
  description: string | null;
  isWriteable: boolean;
}
