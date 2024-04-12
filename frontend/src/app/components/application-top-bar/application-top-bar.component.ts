import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { filter, map, Observable, of, switchMap } from 'rxjs';
import { ConfigService } from '../../config.service';
import { NavigationEnd, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { getFullNameFromUser } from '../../shared/types/model/User';

@Component({
  selector: 'app-application-top-bar',
  templateUrl: './application-top-bar.component.html',
  styleUrls: ['./application-top-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationTopBarComponent implements OnInit {
  userFullName: string = '';
  menuIsOpen = false;
  teamManagementVisible$: Observable<boolean> | undefined;

  constructor(
    private oauthService: OAuthService,
    private userService: UserService,
    private configService: ConfigService,
    private router: Router,
    private readonly cd: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.configService.config$
      .pipe(
        map((config) => {
          if (config.activeProfile === 'staging') {
            document.getElementById('okrTopbar')!.style.backgroundColor = '#ab31ad';
          }
        }),
      )
      .subscribe();

    this.initUserFullName();
    this.initTeamManagementVisible();
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

  logOut() {
    const currentUrlTree = this.router.createUrlTree([], { queryParams: {} });
    this.router.navigateByUrl(currentUrlTree).then(() => {
      this.oauthService.logOut();
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
