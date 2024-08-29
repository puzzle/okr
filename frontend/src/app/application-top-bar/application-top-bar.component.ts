import { ChangeDetectionStrategy, Component, Input, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, map, of, ReplaySubject, Subscription, switchMap } from 'rxjs';
import { ConfigService } from '../config.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TeamManagementComponent } from '../shared/dialog/team-management/team-management.component';
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
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  logOut() {
    this.oauthService.logoff().subscribe();
  }

  username() {
    return this.oauthService.getUserData().pipe(map((user) => user?.name || ''));
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
