import { ChangeDetectionStrategy, Component, Input, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, ReplaySubject, Subscription } from 'rxjs';
import { ConfigService } from '../config.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TeamManagementComponent } from '../shared/dialog/team-management/team-management.component';

import { Router } from '@angular/router';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { isMobileDevice } from '../shared/common';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Component({
  selector: 'app-application-top-bar',
  templateUrl: './application-top-bar.component.html',
  styleUrls: ['./application-top-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationTopBarComponent implements OnInit, OnDestroy {
  username: ReplaySubject<string> = new ReplaySubject();
  menuIsOpen = false;

  @Input()
  hasAdminAccess!: ReplaySubject<boolean>;
  logoSrc$ = new BehaviorSubject<String>('assets/images/empty.svg');
  private dialogRef!: MatDialogRef<TeamManagementComponent> | undefined;
  private subscription?: Subscription;

  constructor(
    private oauthService: OidcSecurityService,
    private configService: ConfigService,
    private dialog: MatDialog,
    private router: Router,
    private refreshDataService: RefreshDataService,
  ) {}

  ngOnInit(): void {
    this.subscription = this.configService.config$.subscribe({
      next: (config) => {
        if (config.logo) {
          this.logoSrc$.next(config.logo);
        }
      },
    });

    // if (this.oauthService.isAuthenticated()) {
    //   this.username.next(this.oauthService.getUserData()['name']);
    // }
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  logOut() {
    // const currentUrlTree = this.router.createUrlTree([], { queryParams: {} });
    // this.router.navigateByUrl(currentUrlTree).then(() => {
    this.oauthService.logoff();
    // });
  }

  openTeamManagement() {
    const dialogConfig = isMobileDevice()
      ? {
          maxWidth: '100vw',
          maxHeight: '100vh',
          height: '100vh',
          width: '100vw',
        }
      : {
          width: '45em',
          height: 'auto',
        };
    if (!this.dialogRef) {
      this.dialogRef = this.dialog.open(TeamManagementComponent, {
        height: dialogConfig.height,
        width: dialogConfig.width,
        maxHeight: dialogConfig.maxHeight,
        maxWidth: dialogConfig.maxWidth,
      });
      this.dialogRef.afterClosed().subscribe(() => {
        this.dialogRef = undefined;
        this.refreshDataService.markDataRefresh();
      });
    }
  }
}
