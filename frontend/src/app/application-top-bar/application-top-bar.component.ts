import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {OAuthService} from 'angular-oauth2-oidc';
import {map} from 'rxjs';
import {ConfigService} from '../config.service';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
// import { TeamManagementComponent } from '../shared/dialog/team-management/team-management.component';
import {Router} from '@angular/router';
import {RefreshDataService} from '../shared/services/refresh-data.service';
import {isMobileDevice} from '../shared/common';
import {UserService} from '../shared/services/user.service';
import {getFullNameFromUser} from '../shared/types/model/User';
import {AddEditTeamDialog} from "../shared/dialog/team-management/add-edit-team-dialog.component";

@Component({
  selector: 'app-application-top-bar',
  templateUrl: './application-top-bar.component.html',
  styleUrls: ['./application-top-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ApplicationTopBarComponent implements OnInit {
  userFullName: string = '';
  menuIsOpen = false;

  private dialogRef!: MatDialogRef<AddEditTeamDialog> | undefined;

  constructor(
    private oauthService: OAuthService,
    private userService: UserService,
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

    this.userFullName = getFullNameFromUser(this.userService.getCurrentUser());
  }

  logOut() {
    const currentUrlTree = this.router.createUrlTree([], { queryParams: {} });
    this.router.navigateByUrl(currentUrlTree).then(() => {
      this.oauthService.logOut();
    });
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
      this.dialogRef = this.dialog.open(AddEditTeamDialog, {
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
