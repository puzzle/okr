import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-key-result-detail',
  templateUrl: './key-result-detail.component.html',
  styleUrls: ['./key-result-detail.component.scss'],
})
export class KeyResultDetailComponent {
  constructor(private location: Location) {}

  navigateBack() {
    this.location.back();
  }
}
