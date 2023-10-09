import { Component, Input } from '@angular/core';
import { KeyResultDTO } from '../shared/types/DTOs/KeyResultDTO';

@Component({
  selector: 'app-keyresult-type',
  templateUrl: './keyresult-type.component.html',
  styleUrls: ['./keyresult-type.component.scss'],
})
export class KeyresultTypeComponent {
  @Input() keyresult!: KeyResultDTO;
  isMetric: boolean = true;
}
