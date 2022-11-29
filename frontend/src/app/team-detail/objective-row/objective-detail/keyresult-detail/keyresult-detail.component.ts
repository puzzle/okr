import { Component, Input, OnInit } from '@angular/core';
import { KeyResult } from '../key-result.service';

@Component({
  selector: 'app-keyresult-detail',
  templateUrl: './keyresult-detail.component.html',
  styleUrls: ['./keyresult-detail.component.scss'],
})
export class KeyresultDetailComponent implements OnInit {
  @Input() keyResult!: KeyResult;
  constructor() {}

  ngOnInit(): void {}
}
