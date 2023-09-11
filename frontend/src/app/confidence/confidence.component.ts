import { Component, Input } from '@angular/core';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';

@Component({
  selector: 'app-confidence',
  templateUrl: './confidence.component.html',
  styleUrls: ['./confidence.component.scss'],
})
export class ConfidenceComponent {
  min: number = 0;
  max: number = 10;
  value: number = 5;
  @Input() edit: boolean = true;
  @Input() keyResult!: KeyresultMin;
}
