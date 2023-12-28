import { Component } from '@angular/core';
import { isMobileDevice } from '../../shared/common';

@Component({
  selector: 'app-team-management-banner',
  templateUrl: './team-management-banner.component.html',
  styleUrl: './team-management-banner.component.scss',
})
export class TeamManagementBannerComponent {
  protected readonly isMobileDevice = isMobileDevice;
}
