import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { TranslateTestingModule } from 'ngx-translate-testing';
import { AuthConfig, OAuthModule, OAuthService } from 'angular-oauth2-oidc';

// FixMe: Fix this test!
xdescribe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

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
          de: require('../assets/i18n/de.json'),
        }),
        OAuthModule.forRoot(),
      ],
      providers: [{ provide: OAuthService, useValue: oauthServiceMock }],
      declarations: [AppComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
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
