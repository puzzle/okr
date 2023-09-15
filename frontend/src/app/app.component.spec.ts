import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { TranslateTestingModule } from 'ngx-translate-testing';
import { AuthConfig, OAuthModule, OAuthService } from 'angular-oauth2-oidc';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { HarnessLoader } from '@angular/cdk/testing';
import { MatDrawerHarness } from '@angular/material/sidenav/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatMenuModule } from '@angular/material/menu';
import { MatCardModule } from '@angular/material/card';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
// @ts-ignore
import * as de from '../assets/i18n/de.json';

// FixMe: Fix this test!
describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let loader: HarnessLoader;

  const oauthServiceMock = {
    oauthService: {
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
    },
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        TranslateTestingModule.withTranslations({
          de: de,
        }),
        OAuthModule.forRoot(),
        HttpClientTestingModule,
      ],
      providers: [{ provide: OAuthService, useValue: oauthServiceMock }],
      declarations: [AppComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  test('should create the app', () => {
    expect(component).toBeTruthy();
  });

  test('should navigate return always false', () => {
    expect(component.navigate('')).toBeFalsy();
  });

  test('should render OKRs navigation item', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('pzsh-nav-item')?.textContent).toContain('OKRs');
  });

  describe('isOverview + isTeam', () => {
    test("should handle '' url ", () => {
      component.currentUrl = '';
      expect(component.isOverview()).toEqual(true);
      expect(component.isTeam()).toEqual(null);
    });

    test("should handle '/' url ", () => {
      component.currentUrl = '/';
      expect(component.isOverview()).toEqual(true);
      expect(component.isTeam()).toEqual(null);
    });

    test("should handle '/objectives/new' url ", () => {
      component.currentUrl = '/objectives/new';
      expect(component.isOverview()).toEqual(true);
      expect(component.isTeam()).toEqual(null);
    });

    test("should handle '/teams' url ", () => {
      component.currentUrl = '/teams';
      expect(component.isOverview()).toEqual(null);
      expect(component.isTeam()).toEqual(true);
    });

    test("should handle '/team' url ", () => {
      component.currentUrl = '/team';
      expect(component.isOverview()).toEqual(null);
      expect(component.isTeam()).toEqual(true);
    });
  });

  describe('convertFalseToNull', () => {
    test('should convert true value to true', () => {
      expect(component.convertFalseToNull(true)).toEqual(true);
    });
    test('should convert false value to null', () => {
      expect(component.convertFalseToNull(false)).toEqual(null);
    });
  });
});

export class TranslateServiceStub {
  setDefaultLang() {}
  use() {}
}

export class OAuthServiceStub {
  loadDiscoveryDocumentAndTryLogin(): Promise<any> {
    return new Promise<any>(() => {});
  }
}

describe('AppComponent2', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let loader: HarnessLoader;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AppComponent],
      imports: [MatMenuModule, MatCardModule, NoopAnimationsModule, RouterTestingModule, HttpClientTestingModule],
      providers: [
        {
          provide: TranslateService,
          useClass: TranslateServiceStub,
        },
        {
          provide: OAuthService,
          useClass: OAuthServiceStub,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;

    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  test('should open and close mat-drawer', () => {
    const drawer = loader.getHarness(MatDrawerHarness.with({ selector: '[data-testid="mat-drawer"]' }));
    console.log(drawer);
  });
});
