import { Component, Input, OnInit } from '@angular/core';
import { KeyresultMin } from '../shared/types/model/KeyresultMin';
import { CheckInMin } from '../shared/types/model/CheckInMin';

@Component({
  selector: 'app-confidence',
  templateUrl: './confidence.component.html',
  styleUrls: ['./confidence.component.scss'],
})
export class ConfidenceComponent implements OnInit {
  min: number = 0;
  max: number = 10;
  value: number = 5;
  @Input() edit: boolean = true;
  @Input() keyResult!: KeyresultMin;

  ngOnInit(): void {
    if (this.keyResult.lastCheckIn == null) {
      this.keyResult.lastCheckIn = { confidence: 5 } as CheckInMin;
    }
  }
}
