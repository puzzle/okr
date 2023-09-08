import { Component, Input, OnInit } from '@angular/core';
import { ObjectiveMin } from '../shared/types/model/ObjectiveMin';

@Component({
  selector: 'app-objective-detail',
  templateUrl: './objective-detail.component.html',
  styleUrls: ['./objective-detail.component.scss'],
})
export class ObjectiveDetailComponent implements OnInit {
  @Input() objective!: ObjectiveMin;
  constructor() {}

  ngOnInit(): void {}
}
