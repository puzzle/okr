import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { map } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';
import { ConfigService } from './config.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent implements OnInit {
  constructor(
    public router: Router,
    private oauthService: OAuthService,
    private configService: ConfigService,
  ) {
    // Try to login via url state
    oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      // if the login failed initialize code flow
      if (!oauthService.hasValidAccessToken()) {
        this.oauthService.initCodeFlow();
      }
      oauthService.setupAutomaticSilentRefresh();
      location.hash = '';
    });
  }

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
}
