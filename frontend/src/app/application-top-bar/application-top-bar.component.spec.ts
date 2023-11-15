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
import { of } from 'rxjs';
import { team1 } from '../shared/testData';

const oAuthMock = {
  getIdentityClaims: jest.fn(),
  logOut: jest.fn(),
  hasValidIdToken: jest.fn(),
};

const dialogMock = {
  open: jest.fn(),
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
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationTopBarComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('logout function should get called on button click', async () => {
    const harness = await loader.getHarness(MatMenuHarness);
    await harness.open();
    fixture.detectChanges();
    harness.getItems().then((items) => {
      items[0].click();
      expect(oAuthMock.logOut).toBeCalledTimes(1);
    });
  });

  it('should make call to dialog object when opening team management dialog', async () => {
    jest.spyOn(dialogMock, 'open').mockReturnValue({ afterClosed: () => of(team1) });
    component.openTeamManagement();
    expect(dialogMock.open).toHaveBeenCalled();
  });
});
