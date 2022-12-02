import { Component, Input, OnInit } from '@angular/core';
import { Objective } from '../../services/objective.service';
import { Observable } from 'rxjs';
import { KeyResultMeasure, KeyResultService } from '../../services/key-result.service';

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