import { Component, OnInit } from '@angular/core';
import { KeyResult } from '../models/KeyResult';
import { MenuEntry } from '../models/MenuEntry';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
})
export class ObjectiveDetailComponent implements OnInit {
  constructor() {}
  keyResults: KeyResult[] = [
    {
      id: 0,
      title: 'title',
      created: '01.01.2022',
      ownerFirstname: 'Firstname',
      ownerLastname: 'lastname',
      progress: 60,
      details: 'details',
      lastMeasure: '',
    },
    {
      id: 1,
      title: 'title',
      created: '01.01.2022',
      ownerFirstname: 'Firstname',
      ownerLastname: 'lastname',
      progress: 60,
      details: 'details',
      lastMeasure: '',
    },
    {
      id: 2,
      title: 'title',
      created: '01.01.2022',
      ownerFirstname: 'Firstname',
      ownerLastname: 'lastname',
      progress: 60,
      details: 'details',
      lastMeasure: '',
    },
  ];

  menuEntries: MenuEntry[] = [
    { displayName: 'Resultat bearbeiten', routeLine: 'result/add' },
    { displayName: 'Resultat duplizieren', routeLine: 'objective/edit' },
    { displayName: 'Details einsehen', routeLine: 'result/add' },
    { displayName: 'Resultat löschen', routeLine: 'result/add' },
    { displayName: 'Messung hinzufügen', routeLine: 'result/add' },
  ];

  ngOnInit(): void {}
}
