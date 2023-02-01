import { Component, Input, OnInit } from '@angular/core';
import {
  KeyResultMeasure,
  KeyResultService,
} from 'src/app/shared/services/key-result.service';
import { Goal } from '../../../services/goal.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-key-result-description',
  templateUrl: './key-result-description.component.html',
  styleUrls: ['./key-result-description.component.scss'],
})
export class KeyResultDescriptionComponent implements OnInit {
  @Input() goal!: Goal;
  keyResultMeasures$!: Observable<KeyResultMeasure>;

  constructor(private keyResultService: KeyResultService) {}

  ngOnInit() {
    this.keyResultMeasures$ = this.keyResultService.getKeyResultById(
      this.goal.keyresult.id
    );
  }

  calculateEffectiveValue(
    basicValue: number,
    targetValue: number,
    progress: number
  ): number {
    let range = targetValue - basicValue;
    let percentValue = (range / 100) * progress;
    let lowerBound = Math.min(basicValue, targetValue);
    return Math.abs(percentValue + lowerBound);
  }
}
