import { Component, Input, OnInit } from '@angular/core';
import { Objective } from '../../objective.service';
import { KeyResult, KeyResultService } from './key-result.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
})
export class ObjectiveDetailComponent implements OnInit {
  @Input() objective!: Objective;
  keyResultList!: Observable<KeyResult[]>;

  constructor(private _keyResultService: KeyResultService) {}

  ngOnInit(): void {
    this.keyResultList = this._keyResultService.getKeyResultsOfObjective(
      this.objective.id
    );
  }
}
