import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { map } from 'rxjs';
import { ConfigService } from '../../config.service';
// import { TeamManagementComponent } from '../shared/dialog/team-management/team-management.component';
import { Router } from '@angular/router';
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

  constructor(
    private oauthService: OAuthService,
    private userService: UserService,
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

    this.userFullName = getFullNameFromUser(this.userService.getCurrentUser());
  }

  logOut() {
    const currentUrlTree = this.router.createUrlTree([], { queryParams: {} });
    this.router.navigateByUrl(currentUrlTree).then(() => {
      this.oauthService.logOut();
    });
  }
}
