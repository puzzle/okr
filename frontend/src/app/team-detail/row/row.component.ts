import { Component, Input, OnInit } from '@angular/core';
import { Menu } from '@angular/cdk/menu';
import { MenuEntry } from 'src/app/models/MenuEntry';
import { Objective } from '../../models/Objective';
import { RowObject } from 'src/app/models/RowObject';

@Component({
  selector: 'app-row',
  templateUrl: './row.component.html',
  styleUrls: ['./row.component.scss'],
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
