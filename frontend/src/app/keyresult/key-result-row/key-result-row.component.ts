import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import {Objective} from "../../services/objective.service";
import {MenuEntry} from "../../types/menu-entry";
import {KeyResultMeasure} from "../../services/key-result.service";

@Component({
  selector: 'app-keyresult-row',
  templateUrl: './key-result-row.component.html',
  styleUrls: ['./key-result-row.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class KeyResultRowComponent implements OnInit {
  @Input() element!: KeyResultMeasure;
  @Input() menuEntries!: MenuEntry[];
  @Input() information!: string[];
  progressRecord!: Record<string, boolean>;

  constructor() {}

  ngOnInit(): void {
    this.progressRecord = {
      'progress-bar-bad': this.element?.progress < 40,
      'progress-bar-medium': this.element?.progress < 70,
      'progress-bar-good': this.element?.progress <= 100,
    };
  }

  public addResult() {
    console.log('Resultat hinzufügen');
  }

  public editGoal() {
    console.log('Ziel bearbeiten');
  }

  public duplicateGoal() {
    console.log('Ziel duplizieren');
  }

  public deleteGoal() {
    console.log('Ziel löschen');
  }

  test() {
    console.log(new Date());
  }
}