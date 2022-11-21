import { Component, OnInit } from '@angular/core';

export interface Objectives {
  id: number;
  title: String;
  ownerId: number;
  ownerFirstname: String;
  ownerLastname: String;
  quarterId: number;
  quarterNumber: number;
  quarterYear: number;
  description: String;
  progress: number;
}

export interface TeamObjectives {
  id: number;
  name: string;
  objectives: Objectives[];
}

@Component({
  selector: 'app-team-detail',
  templateUrl: './team-detail.component.html',
  styleUrls: ['./team-detail.component.scss'],
})
export class TeamDetailComponent implements OnInit {
  objectives: Array<Objectives> = [
    {
      id: 3,
      title: 'Wir erzielen eine ausgezeichnete Wirtschaftlichkeit',
      ownerId: 2,
      ownerFirstname: 'Rudi',
      ownerLastname: 'Grochde',
      quarterId: 5,
      quarterNumber: 3,
      quarterYear: 2022,
      description: 'Sehr wichtig',
      progress: 5,
    },
  ];
  teamObjectives: TeamObjectives = {
    id: 3,
    name: 'dev/ruby',
    objectives: this.objectives,
  };
  constructor() {
    //Daten manuell einfügen
  }

  ngOnInit(): void {}
}
