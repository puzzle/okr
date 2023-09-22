import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter, map, Observable } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';
import { ConfigService } from './config.service';
import { NotifierService } from './shared/services/notifier.service';
import { drawerRoutes, ROUTE_PARAM_REGEX } from './shared/constantLibary';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent implements OnInit, OnDestroy {
  isEnvStaging$: Observable<boolean>;
  drawerOpen: boolean = false;
  sidenavContentInformation!: { id: number; type: string };

  constructor(
    public router: Router,
    private oauthService: OAuthService,
    private configService: ConfigService,
    private notifierService: NotifierService,
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
    this.isEnvStaging$ = this.configService.config$.pipe(
      map((config) => {
        return config.activeProfile === 'staging';
      }),
    );
  }

  ngOnInit(): void {
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        map((event) => event as NavigationEnd),
      )
      .subscribe((event) => {
        drawerRoutes.forEach((route) => {
          if (event.url.startsWith(`/${route}/`)) {
            const match = event.url.match(ROUTE_PARAM_REGEX);
            if (match) {
              const id = parseInt(match[1]);
              this.sidenavContentInformation = { id: id, type: route };
              this.openDrawer();
            }
          }
        });
      });

    this.notifierService.closeDetailSubject.subscribe(() => {
      this.closeDrawer();
    });
  }

  ngOnDestroy() {
    this.notifierService.closeDetailSubject.unsubscribe();
  }

  enableScrolling() {
    document.body.setAttribute('style', 'overflow: visible;');
  }

  openDrawer() {
    this.drawerOpen = true;
    this.disableScrolling();
  }

  closeDrawer() {
    this.drawerOpen = false;
    this.router.navigate(['/']);
  }

  private disableScrolling() {
    document.body.setAttribute('style', 'overflow: hidden;');
  }
}
