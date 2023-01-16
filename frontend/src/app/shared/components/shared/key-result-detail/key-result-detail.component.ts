import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-key-result-detail',
  templateUrl: './key-result-detail.component.html',
  styleUrls: ['./key-result-detail.component.scss'],
})
export class KeyResultDetailComponent {
  constructor(private router: Router) {}

  navigateBack() {
    this.router.navigate(['/dashboard']);
  }
}
