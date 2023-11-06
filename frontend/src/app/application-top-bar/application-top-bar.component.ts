import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {OAuthService} from 'angular-oauth2-oidc';
import {map, Subject} from 'rxjs';
import {ConfigService} from '../config.service';
import {Router} from '@angular/router';
import { username } from '../shared/common';

@Component({
  selector: 'app-application-top-bar',
  templateUrl: './application-top-bar.component.html',
  styleUrls: ['./application-top-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationTopBarComponent implements OnInit {
  @Input()
  username!: Subject<string>;

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
            document.getElementById('pzsh-topbar')!.style.backgroundColor = '#ab31ad';
          }
        }),
      )
      .subscribe();
  }
  logOut() {
    const currentUrlTree = this.router.createUrlTree([], { queryParams: {} });
    this.router.navigateByUrl(currentUrlTree).then(() => {
      this.oauthService.logOut();
    });
  }

  protected readonly username = username;
}
