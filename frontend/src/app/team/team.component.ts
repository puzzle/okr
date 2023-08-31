import { Component, Input, OnInit } from '@angular/core';
import { OverviewEntity } from '../model/OverviewEntity';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
})
export class TeamComponent implements OnInit {
  @Input() overviewEntity!: OverviewEntity;

  constructor() {
    /* TODO document why this constructor is empty */
  }

  ngOnInit(): void {
    // TODO document why this method 'ngOnInit' is empty
  }
}
