import { Component, OnInit } from '@angular/core';

export interface Objectives {
  id: number;
  title: String;
  ownerId: number;
  ownerFirstname: String;
  ownerLastname: String;
  teamId: number;
  teamName: String;
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
  constructor() {}

  ngOnInit(): void {}
}
