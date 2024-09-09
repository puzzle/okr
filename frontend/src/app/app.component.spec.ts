import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { TranslateTestingModule } from 'ngx-translate-testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
// @ts-ignore
import * as de from '../assets/i18n/de.json';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatSidenavModule } from '@angular/material/sidenav';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { of } from 'rxjs';
import { OverviewComponent } from './overview/overview.component';
import { ObjectiveDetailComponent } from './objective-detail/objective-detail.component';
import { CommonModule } from '@angular/common';
import { StsConfigLoader } from 'angular-auth-oidc-client';

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
        RouterModule.forRoot(routes),
        HttpClientTestingModule,
        TranslateTestingModule.withTranslations({
          de: de,
        }),
        MatSidenavModule,
        NoopAnimationsModule,
        CommonModule,
      ],
      providers: [
        {
          provide: StsConfigLoader,
          useValue: {
            loadConfig: () => of({}),
            loadConfigs: () => of({}),
          },
        },
      ],
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
