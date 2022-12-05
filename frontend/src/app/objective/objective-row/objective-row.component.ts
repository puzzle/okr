import { Component, Input, OnInit } from '@angular/core';
import { Objective } from '../../shared/services/objective.service';
import { MenuEntry } from '../../shared/types/menu-entry';

@Component({
  selector: 'app-objective-row',
  templateUrl: './objective-row.component.html',
  styleUrls: ['./objective-row.component.scss'],
})
export class ObjectiveRowComponent implements OnInit {
  @Input() objective!: Objective;

  menuEntries: MenuEntry[] = [
    { displayName: 'Resultat hinzufügen', routeLine: 'result/add' },
    { displayName: 'Ziel bearbeiten', routeLine: 'objective/edit' },
    { displayName: 'Ziel duplizieren', routeLine: 'objective/duplicate' },
    { displayName: 'Ziel löschen', routeLine: 'objective/delete' },
  ];

  progressRecord!: Record<string, boolean>;

  constructor() {}

  ngOnInit(): void {
    this.progressRecord = {
      'progress-bar-bad': this.objective?.progress < 40,
      'progress-bar-medium': this.objective?.progress < 70,
      'progress-bar-good': this.objective?.progress <= 100,
    };
  }

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
