import { ChangeDetectionStrategy, Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter, map, Observable } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { version } from './version';
import { ConfigService } from './config.service';
import { RouteService } from './shared/services/route.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent implements OnInit {
  currentUrl: string = '/';
  isEnvStaging$: Observable<boolean>;

  @ViewChild('okrTopbar')
  okrTopbar: ElementRef | undefined;
  offsetFlag: boolean = true;

  constructor(
    private router: Router,
    private translate: TranslateService,
    private routeService: RouteService,
    private oauthService: OAuthService,
    private configService: ConfigService
  ) {
    translate.setDefaultLang('de');
    translate.use('de');

    // Try to login via url state
    oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      // if the login failed initialize code flow
      if (!oauthService.hasValidAccessToken()) {
        this.oauthService.initCodeFlow();
      }
      oauthService.setupAutomaticSilentRefresh();
      location.hash = '';
    });
    this.isEnvStaging$ = this.configService.config$.pipe(
      map((config) => {
        return config.activeProfile === 'staging';
      })
    );
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

  @HostListener('document:wheel', ['$event'])
  getScrollHeight(event: Event) {
    console.log('moin');
    if (window.scrollY > 0) this.offsetFlag = false;
    else this.offsetFlag = true;
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

  protected readonly version = version;
}
