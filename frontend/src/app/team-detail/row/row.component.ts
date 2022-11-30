import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { MenuEntry } from 'src/app/models/MenuEntry';
import { RowObject } from 'src/app/models/RowObject';
import { timestamp } from 'rxjs';

@Component({
  selector: 'app-row',
  templateUrl: './row.component.html',
  styleUrls: ['./row.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class RowComponent implements OnInit {
  @Input() element!: RowObject;
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
