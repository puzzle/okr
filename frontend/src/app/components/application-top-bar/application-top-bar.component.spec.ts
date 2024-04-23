import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationTopBarComponent } from './application-top-bar.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DateTimeProvider, OAuthLogger, OAuthService, UrlHelperService } from 'angular-oauth2-oidc';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { MatMenuModule } from '@angular/material/menu';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatMenuHarness } from '@angular/material/menu/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { NavigationEnd, Router } from '@angular/router';
import { of } from 'rxjs';
import { testUser } from '../../shared/testData';
import { UserService } from '../../services/user.service';
import { ConfigService } from '../../services/config.service';

const oAuthMock = {
  getIdentityClaims: jest.fn(),
  logOut: jest.fn(),
  hasValidIdToken: jest.fn(),
};

const dialogMock = {
  open: jest.fn(),
};

const routerMock = {
  events: of(new NavigationEnd(1, '', '')),
  navigateByUrl: jest.fn(),
};

const userServiceMock = {
  getCurrentUser: () => testUser,
};

const configServiceMock = {
  config$: of({}),
};

describe('ApplicationHeaderComponent', () => {
  let component: ApplicationTopBarComponent;
  let fixture: ComponentFixture<ApplicationTopBarComponent>;
  let loader: HarnessLoader;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MatMenuModule, NoopAnimationsModule, MatDialogModule],
      declarations: [ApplicationTopBarComponent],
      providers: [
        { provide: OAuthService, useValue: oAuthMock },
        { provide: HttpClient },
        { provide: HttpHandler },
        { provide: UrlHelperService },
        { provide: OAuthLogger },
        { provide: DateTimeProvider },
        {
          provide: MatDialog,
          useValue: dialogMock,
        },
        {
          provide: Router,
          useValue: routerMock,
        },
        {
          provide: UserService,
          useValue: userServiceMock,
        },
        {
          provide: ConfigService,
          useValue: configServiceMock,
        },
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationTopBarComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    component.ngOnInit();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set full name from user service', () => {
    expect(component.userFullName).toBe('Bob Baumeister');
  });

  it('logout function should get called on button click', async () => {
    routerMock.navigateByUrl.mockReturnValue(of().toPromise());
    const harness = await loader.getHarness(MatMenuHarness);
    await harness.open();
    fixture.detectChanges();
    harness.getItems().then((items) => {
      items[0].click();
      expect(oAuthMock.logOut).toBeCalledTimes(1);
    });
  });
});
