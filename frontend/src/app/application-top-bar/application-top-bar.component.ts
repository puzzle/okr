import { ChangeDetectionStrategy, Component } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

@Component({
  selector: 'app-application-top-bar',
  templateUrl: './application-top-bar.component.html',
  styleUrls: ['./application-top-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationTopBarComponent {
  constructor(private oauthService: OAuthService) {}
  logOut() {
    this.oauthService.logOut();
  }

  getUserName() {
    return this.oauthService.getIdentityClaims()['name'];
  }
}
