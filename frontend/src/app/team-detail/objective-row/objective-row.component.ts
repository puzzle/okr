import { Component, Input, OnInit } from '@angular/core';
import { Objective } from '../objective.service';

@Component({
  selector: 'app-objective-row',
  templateUrl: './objective-row.component.html',
  styleUrls: ['./objective-row.component.scss'],
})
export class ObjectiveRowComponent implements OnInit {
  @Input() objective!: Objective;

  constructor() {}

  ngOnInit(): void {}
}
