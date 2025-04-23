import { Component } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-overview-banner',
  templateUrl: './overview-banner.component.html',
  styleUrl: './overview-banner.component.scss',
  standalone: false
})
export class OverviewBannerComponent {
  quarterLabel$: BehaviorSubject<string> = new BehaviorSubject<string>('');

  panelOpenState = false;
}
