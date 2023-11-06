import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { map } from 'rxjs';
import { ConfigService } from '../config.service';

@Component({
  selector: 'app-application-top-bar',
  templateUrl: './application-top-bar.component.html',
  styleUrls: ['./application-top-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationTopBarComponent implements OnInit {
  constructor(
    private oauthService: OAuthService,
    private configService: ConfigService,
  ) {}

  ngOnInit(): void {
    this.configService.config$
      .pipe(
        map((config) => {
          if (config.activeProfile === 'staging') {
            document.getElementById('pzsh-topbar')!.style.backgroundColor = '#ab31ad';
          }
        }),
      )
      .subscribe();
  }
  logOut() {
    this.oauthService.logOut();
  }

  getUserName() {
    return this.oauthService.getIdentityClaims()['name'];
  }
}
