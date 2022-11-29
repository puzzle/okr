import { Component, Input, OnInit } from '@angular/core';
import { KeyResultMeasure } from '../key-result.service';

@Component({
  selector: 'app-keyresult-detail',
  templateUrl: './keyresult-detail.component.html',
  styleUrls: ['./keyresult-detail.component.scss'],
})
export class KeyresultDetailComponent implements OnInit {
  @Input() keyResult!: KeyResultMeasure;
  constructor() {}

  ngOnInit(): void {}
}
