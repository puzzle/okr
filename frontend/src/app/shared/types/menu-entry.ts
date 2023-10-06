import { ComponentType } from '@angular/cdk/overlay';

export interface MenuEntry {
  displayName: string;
  dialog?: {
    dialog: ComponentType<any>;
    data: any;
  };
  route?: string;
}
