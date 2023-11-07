import { ComponentFixture, fakeAsync, TestBed, waitForAsync } from '@angular/core/testing';

import { ApplicationTopBarComponent } from './application-top-bar.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DateTimeProvider, OAuthLogger, OAuthService, UrlHelperService } from 'angular-oauth2-oidc';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { MatMenuModule } from '@angular/material/menu';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatMenuHarness } from '@angular/material/menu/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import {MatDialogModule} from "@angular/material/dialog";

const oAuthMock = {
  getIdentityClaims: jest.fn(),
  logOut: jest.fn(),
  hasValidIdToken: jest.fn(),
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
});
