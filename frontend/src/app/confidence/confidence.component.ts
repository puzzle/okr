import { Component, Input, OnInit } from '@angular/core';
import { KeyResultOrdinalMin } from '../shared/types/model/KeyResultOrdinalMin';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';

@Component({
  selector: 'app-confidence',
  templateUrl: './confidence.component.html',
  styleUrls: ['./confidence.component.scss'],
})
export class ConfidenceComponent implements OnInit {
  min: number = 0;
  max: number = 10;
  value: number = 5;
  @Input() showSlider: boolean = true;
  @Input() keyResult!: KeyresultMin;

  constructor() {}

  ngOnInit(): void {}
}
