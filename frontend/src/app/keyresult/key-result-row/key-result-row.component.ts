import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { MenuEntry } from '../../types/menu-entry';
import { KeyResultMeasure } from '../../services/key-result.service';

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
  progressPercentage!: number;
  progressRecord!: Record<string, boolean>;

  constructor() {}

  ngOnInit(): void {
    this.progressPercentage = Math.round(
      (this.element?.measure.value /
        (this.element?.targetValue - this.element?.basicValue)) *
        100
    );
    this.progressRecord = {
      'progress-bar-bad': this.progressPercentage < 40,
      'progress-bar-medium': this.progressPercentage < 70,
      'progress-bar-good': this.progressPercentage <= 100,
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
