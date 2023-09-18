import { objective } from '../../testData';

export class SidenavModel {
  id: number;
  type: 'objective' | 'keyResult';

  constructor(id: number, type: 'objective' | 'keyResult') {
    this.id = id;
    this.type = type;
  }
}
