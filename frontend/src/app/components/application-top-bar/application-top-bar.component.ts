import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { BehaviorSubject, filter, Observable, of, Subscription, switchMap } from 'rxjs';
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
  teamManagementVisible$: Observable<boolean> | undefined;
  logoSrc$ = new BehaviorSubject<String>('assets/images/empty.svg');
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
      },
    });

    this.initUserFullName();
    this.initTeamManagementVisible();
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

  private initTeamManagementVisible() {
    this.teamManagementVisible$ = this.router.events.pipe(
      filter((e): e is NavigationEnd => e instanceof NavigationEnd),
      switchMap(() => {
        return of(this.router.url.split('?')[0] === '/');
      }),
    );
  }
}
