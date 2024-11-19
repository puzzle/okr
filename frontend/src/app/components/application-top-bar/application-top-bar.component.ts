import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { BehaviorSubject, Subscription } from 'rxjs';
import { ConfigService } from '../../services/config.service';
import { NavigationEnd, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { getFullNameFromUser } from '../../shared/types/model/User';

@Component({
  selector: 'app-application-top-bar',
  templateUrl: './application-top-bar.component.html',
  styleUrls: ['./application-top-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationTopBarComponent implements OnInit, OnDestroy {
  userFullName: string = '';
  menuIsOpen = false;
  logoSrc$ = new BehaviorSubject<String>('assets/images/empty.svg');
  supportSiteUrl$ = new BehaviorSubject<string>('https://en.wikipedia.org/wiki/Objectives_and_key_results');
  private subscription?: Subscription;

  constructor(
    private oauthService: OAuthService,
    private userService: UserService,
    private configService: ConfigService,
    private router: Router,
    private readonly cd: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.subscription = this.configService.config$.subscribe({
      next: (config) => {
        if (config.logo) {
          this.logoSrc$.next(config.logo);
        }
        if (config.supportSiteUrl) {
          this.supportSiteUrl$.next(config.supportSiteUrl);
        }
      },
    });
    this.initUserFullName();
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  logOut() {
    const currentUrlTree = this.router.createUrlTree([], { queryParams: {} });
    this.router.navigateByUrl(currentUrlTree).then(() => {
      this.oauthService.logOut();
    });
  }

  private initUserFullName() {
    // user is loaded on base route resolver. We have to wait until routing is done.
    this.router.events.subscribe((val) => {
      if (!this.userFullName && val instanceof NavigationEnd) {
        this.userFullName = getFullNameFromUser(this.userService.getCurrentUser());
        this.cd.markForCheck();
      }
    });
  }

  protected readonly window = window;
}
