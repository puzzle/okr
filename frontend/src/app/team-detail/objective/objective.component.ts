import { Component, Input, OnInit } from '@angular/core';
import { Objective } from '../team-detail.component';

@Component({
  selector: 'app-objective',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
})
export class ObjectiveComponent implements OnInit {
  @Input()
  objectiv: Objective | undefined;

  constructor() {}

  ngOnInit(): void {}
}
