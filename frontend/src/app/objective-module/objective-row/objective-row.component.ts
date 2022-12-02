import { Component, Input, OnInit } from '@angular/core';
import { Objective } from '../../services/objective.service';

@Component({
  selector: 'app-objective-row',
  templateUrl: './objective-row.component.html',
  styleUrls: ['./objective-row.component.scss'],
})
export class ObjectiveRowComponent implements OnInit {
  @Input() objective!: Objective;

  constructor() {}

  ngOnInit(): void {}

  public addResult() {
    console.log('Keyresult hinzufügen');
  }

  public editGoal() {
    console.log('Objective bearbeiten');
  }

  public duplicateGoal() {
    console.log('Objective duplizieren');
  }

  public deleteGoal() {
    console.log('Objective löschen');
  }
}
