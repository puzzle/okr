import { Component, Input } from '@angular/core';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';

@Component({
  selector: 'app-keyresult',
  templateUrl: './keyresult.component.html',
  styleUrls: ['./keyresult.component.scss'],
})
export class KeyresultComponent {
  @Input() keyResult!: KeyresultMin;

  constructor() {}
}
