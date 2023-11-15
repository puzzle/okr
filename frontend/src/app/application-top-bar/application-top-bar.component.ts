import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { map, ReplaySubject } from 'rxjs';
import { ConfigService } from '../config.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-application-top-bar',
  templateUrl: './application-top-bar.component.html',
  styleUrls: ['./application-top-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationTopBarComponent implements OnInit {
  username: ReplaySubject<string> = new ReplaySubject();
  menuIsOpen = false;

  constructor(
    private oauthService: OAuthService,
    private configService: ConfigService,
    private router: Router,
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

    if (this.oauthService.hasValidIdToken()) {
      this.username.next(this.oauthService.getIdentityClaims()['name']);
    }
  }
  logOut() {
    const currentUrlTree = this.router.createUrlTree([], { queryParams: {} });
    this.router.navigateByUrl(currentUrlTree).then(() => {
      this.oauthService.logOut();
    });
  }
}
