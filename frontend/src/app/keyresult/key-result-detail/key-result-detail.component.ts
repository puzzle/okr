import { Component, Input, OnInit } from '@angular/core';
import { KeyResultMeasure } from '../../services/key-result.service';

@Component({
  selector: 'app-key-result-detail',
  templateUrl: './key-result-detail.component.html',
  styleUrls: ['./key-result-detail.component.scss'],
})
export class KeyResultDetailComponent implements OnInit {
  @Input() keyResult!: KeyResultMeasure;
  constructor() {}

  ngOnInit(): void {}
}
