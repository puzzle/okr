import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { OAuthModule, OAuthService } from 'angular-oauth2-oidc';
import { HttpClientTestingModule } from '@angular/common/http/testing';
// @ts-ignore
import * as de from '../assets/i18n/de.json';
import { MatSidenavModule } from '@angular/material/sidenav';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { Routes } from '@angular/router';
import { OverviewComponent } from './components/overview/overview.component';
import { ObjectiveDetailComponent } from './components/objective-detail/objective-detail.component';
import { CommonModule } from '@angular/common';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { of } from 'rxjs';

const oAuthServiceMock = {};

const routes: Routes = [{
  path: '',
  component: OverviewComponent,
  children: [{
    path: 'objective/:id',
    component: ObjectiveDetailComponent,
    pathMatch: 'full'
  }]
}];

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        HttpClientTestingModule,
        TranslateModule.forRoot({
          loader: {
            provide: TranslateLoader,
            useValue: {
              getTranslation: () => of(de)
            }
          }
        }),
        OAuthModule.forRoot(),
        MatSidenavModule,
        NoopAnimationsModule,
        CommonModule
      ],
      providers: [{
        provide: OAuthService,
        useValue: oAuthServiceMock
      }],
      declarations: [AppComponent,
        OverviewComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(AppComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();

        fixture.detectChanges();
      });
  });

  test('should create the app', () => {
    expect(component)
      .toBeTruthy();
  });
});
