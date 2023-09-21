import { ComponentFixture, fakeAsync, TestBed, tick, waitForAsync } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { TranslateTestingModule } from 'ngx-translate-testing';
import { AuthConfig, OAuthModule, OAuthService } from 'angular-oauth2-oidc';
import { MatDrawerContainerHarness, MatDrawerHarness } from '@angular/material/sidenav/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
// @ts-ignore
import * as de from '../assets/i18n/de.json';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { By } from '@angular/platform-browser';
import { MatDrawer, MatSidenavModule } from '@angular/material/sidenav';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

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
describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let loader: HarnessLoader;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        TranslateTestingModule.withTranslations({
          de: de,
        }),
        OAuthModule.forRoot(),
        MatSidenavModule,
        NoopAnimationsModule,
      ],
      providers: [{ provide: OAuthService, useValue: oauthServiceMock }],
      declarations: [AppComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    loader = TestbedHarnessEnvironment.loader(fixture);

    fixture.detectChanges();
  });

  test('should create the app', () => {
    expect(component).toBeTruthy();
  });

  test('should navigate return always false', () => {
    expect(component.navigate('')).toBeFalsy();
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

  describe('Mat-drawer', () => {
    test('should open and close mat-drawer', async () => {
      await loader.getHarness(MatDrawerContainerHarness).then(async (drawerContainer: MatDrawerContainerHarness) => {
        drawerContainer.getDrawers().then(async (drawers: MatDrawerHarness[]) => {
          const drawer: MatDrawerHarness = drawers[0];
          component.openDrawer();
          fixture.detectChanges();
          expect(await drawer.isOpen()).toEqual(true);
          component.closeDrawer();
          fixture.detectChanges();
          expect(await drawer.isOpen()).toEqual(false);
        });
      });
    });

    test.each([
      ['keydown.enter', false],
      ['keydown.space', false],
      ['keydown.escape', false],
      ['', true],
    ])('close on keyPress', async (event: string, isOpen: boolean) => {
      await loader.getHarness(MatDrawerContainerHarness).then(async (drawerContainer) => {
        drawerContainer.getDrawers().then(async (drawers) => {
          let drawer = drawers[0];
          component.openDrawer();

          expect(await drawer.isOpen()).toEqual(true);
          fixture.debugElement.triggerEventHandler(event, { bubbles: true });
          fixture.detectChanges();

          expect(await drawer.isOpen()).toEqual(isOpen);
        });
      });
    });
  });
});
