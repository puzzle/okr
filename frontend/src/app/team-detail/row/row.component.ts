import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { MenuEntry } from 'src/app/models/MenuEntry';
import { RowObject } from 'src/app/models/RowObject';

@Component({
  selector: 'app-row',
  templateUrl: './row.component.html',
  styleUrls: ['./row.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class RowComponent implements OnInit {
  @Input() element!: RowObject;
  @Input() menuEntries!: MenuEntry[];

  constructor() {}

  ngOnInit(): void {}

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
}
