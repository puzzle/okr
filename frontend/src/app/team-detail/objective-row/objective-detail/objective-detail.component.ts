import { Component, Input, OnInit } from '@angular/core';
import { Objective } from '../../objective.service';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
})
export class ObjectiveDetailComponent implements OnInit {
  @Input() objective!: Objective;

  constructor() {}

  ngOnInit(): void {}
}
