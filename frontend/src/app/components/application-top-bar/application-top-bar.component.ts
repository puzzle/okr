import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { BehaviorSubject, Subscription } from 'rxjs';
import { ConfigService } from '../../services/config.service';
import { NavigationEnd, Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-application-top-bar',
  templateUrl: './application-top-bar.component.html',
  styleUrls: ['./application-top-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class ApplicationTopBarComponent implements OnInit, OnDestroy {
  userFullName = '';

  menuIsOpen = false;

  logoSrc$ = new BehaviorSubject<string>('assets/images/empty.svg');

  helpSiteUrl = new BehaviorSubject<string>('https://en.wikipedia.org/wiki/Objectives_and_key_results');

  private subscription?: Subscription;

  constructor(
    private oAuthService: OAuthService,
    private userService: UserService,
    private configService: ConfigService,
    private router: Router,
    private readonly cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.subscription = this.configService.config$.subscribe({
      next: (config) => {
        if (config.logo) {
          this.logoSrc$.next(config.logo);
        }
        if (config.helpSiteUrl) {
          this.helpSiteUrl.next(config.helpSiteUrl);
        }
      }
    });
    this.initUserFullName();
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  logOut() {
    const currentUrlTree = this.router.createUrlTree([], { queryParams: {} });
    this.router.navigateByUrl(currentUrlTree)
      .then(() => {
        this.oAuthService.logOut();
      });
  }

  private initUserFullName() {
    // user is loaded on base route resolver. We have to wait until routing is done.
    this.router.events.subscribe((val) => {
      if (!this.userFullName && val instanceof NavigationEnd) {
        this.userFullName = this.userService.getCurrentUser().fullName;
        this.cd.markForCheck();
      }
    });
  }

  protected readonly window = window;
}
