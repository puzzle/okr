import { v4 as uuidv4 } from 'uuid';

export const uniqueSuffix = (value: string): string => {
  return `${value}-${uuidv4()}`;
};
