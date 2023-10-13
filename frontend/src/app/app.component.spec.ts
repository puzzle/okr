import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { TranslateTestingModule } from 'ngx-translate-testing';
import { AuthConfig, OAuthModule, OAuthService } from 'angular-oauth2-oidc';
import { HttpClientTestingModule } from '@angular/common/http/testing';
// @ts-ignore
import * as de from '../assets/i18n/de.json';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatSidenavModule } from '@angular/material/sidenav';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NavigationEnd, Routes } from '@angular/router';
import { of } from 'rxjs';
import { OverviewComponent } from './overview/overview.component';
import { ObjectiveDetailComponent } from './objective-detail/objective-detail.component';
import { CommonModule } from '@angular/common';

const oauthServiceMock = {
  configure(environment: AuthConfig): void {},
  initCodeFlow(): void {},
  setupAutomaticSilentRefresh(): void {},
  hasValidAccessToken(): boolean {
    return true;
  },
  loadDiscoveryDocumentAndTryLogin(): Promise<any> {
    this.initCodeFlow();
    return Promise.resolve();
  },
};

const routerMock = {
  root: jest.fn(),
  // Router
  events: of(new NavigationEnd(0, 'http://localhost:4200/objective/2', 'http://localhost:4200/objective/2')),
};

const routes: Routes = [
  {
    path: '',
    component: OverviewComponent,
    children: [{ path: 'objective/:id', component: ObjectiveDetailComponent, pathMatch: 'full' }],
  },
];

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let loader: HarnessLoader;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        HttpClientTestingModule,
        TranslateTestingModule.withTranslations({
          de: de,
        }),
        OAuthModule.forRoot(),
        MatSidenavModule,
        NoopAnimationsModule,
        CommonModule,
      ],
      providers: [{ provide: OAuthService, useValue: oauthServiceMock }],
      declarations: [AppComponent, OverviewComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(AppComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();

        loader = TestbedHarnessEnvironment.loader(fixture);
        fixture.detectChanges();
      });
  });

  test('should create the app', () => {
    expect(component).toBeTruthy();
  });
});
