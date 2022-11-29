import { Component, Input, OnInit } from '@angular/core';
import { Objective } from '../../objective.service';
import { Observable } from 'rxjs';
import { KeyResultMeasure, KeyResultService } from './key-result.service';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
})
export class ObjectiveDetailComponent implements OnInit {
  @Input() objective!: Objective;
  keyResultList!: Observable<KeyResultMeasure[]>;

  constructor(private _keyResultService: KeyResultService) {}

  ngOnInit(): void {
    this.keyResultList = this._keyResultService.getKeyResultsOfObjective(
      this.objective.id
    );
  }
}
