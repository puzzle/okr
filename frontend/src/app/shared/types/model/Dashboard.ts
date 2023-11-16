import { OverviewEntity } from './OverviewEntity';

export interface Dashboard {
  adminAccess: boolean;
  overviews: OverviewEntity[];
}
