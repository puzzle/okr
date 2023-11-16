import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { map, Observable, ReplaySubject } from 'rxjs';
import { ConfigService } from '../config.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TeamManagementComponent } from '../shared/dialog/team-management/team-management.component';

import { Router } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';

@Component({
  selector: 'app-application-top-bar',
  templateUrl: './application-top-bar.component.html',
  styleUrls: ['./application-top-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationTopBarComponent implements OnInit {
  username: ReplaySubject<string> = new ReplaySubject();
  menuIsOpen = false;

  @Input()
  hasAdminAccess!: ReplaySubject<boolean>;
  private dialogRef!: MatDialogRef<TeamManagementComponent> | undefined;

  constructor(
    private oauthService: OAuthService,
    private configService: ConfigService,
    private dialog: MatDialog,
    private router: Router,
    private refreshDataService: RefreshDataService,
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

  openTeamManagement() {
    if (!this.dialogRef) {
      this.dialogRef = this.dialog.open(TeamManagementComponent, {
        width: '45em',
        height: 'auto',
      });
      this.dialogRef.afterClosed().subscribe(() => {
        this.dialogRef = undefined;
        this.refreshDataService.markDataRefresh();
      });
    }
  }
}
