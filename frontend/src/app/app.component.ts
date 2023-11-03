import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { map } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';
import { ConfigService } from './config.service';
import { UserService } from './shared/services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent implements OnInit {
  constructor(
    public router: Router,
    private configService: ConfigService,
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
}
