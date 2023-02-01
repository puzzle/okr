import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter, map } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { RouteService } from './shared/services/route.service';
import { OAuthService } from 'angular-oauth2-oidc';
import { environment } from '../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent implements OnInit {
  currentUrl: string = '/';

  constructor(
    private router: Router,
    private translate: TranslateService,
    private routeService: RouteService,
    private oauthService: OAuthService
  ) {
    translate.setDefaultLang('de');
    translate.use('de');

    oauthService.configure(environment.oauth);

    // Try to login via url state
    oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      // if the login failed initialize code flow
      if (!oauthService.hasValidAccessToken()) {
        this.oauthService.initCodeFlow();
      }
    });
    oauthService.setupAutomaticSilentRefresh();
  }

  ngOnInit(): void {
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        map((event) => event as NavigationEnd)
      )
      .subscribe((event) => {
        this.currentUrl = event.url;
      });
  }

  isOverview(): null | true {
    return this.convertFalseToNull(!this.isTeam());
  }

  isTeam(): null | true {
    return this.convertFalseToNull(this.currentUrl.startsWith('/team'));
  }

  /**
   * Puzzle Shell use `active="null"` instead of `active="false"`!
   */
  convertFalseToNull(value: boolean): true | null {
    return value ? true : null;
  }

  /**
   * Disable Puzzle Shell link handling.
   */
  navigate(location: string): boolean {
    this.routeService.navigate(location);
    return false;
  }

  login() {}
}
