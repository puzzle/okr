import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverviewComponent } from './overview.component';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { overViewEntity1 } from '../../shared/test-data';
import { BehaviorSubject, of, Subject } from 'rxjs';
import { OverviewService } from '../../services/overview.service';
import { AppRoutingModule } from '../../app-routing.module';
import { RefreshDataService } from '../../services/refresh-data.service';
import { authGuard } from '../../guards/auth.guard';
import { ApplicationBannerComponent } from '../../shared/custom/application-banner/application-banner.component';
import { ApplicationTopBarComponent } from '../application-top-bar/application-top-bar.component';
import { DateTimeProvider, OAuthLogger, OAuthService, UrlHelperService } from 'angular-oauth2-oidc';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { CUSTOM_ELEMENTS_SCHEMA, signal } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { OverviewEntity } from '../../shared/types/model/overview-entity';

const overviewService = {
  getOverview: jest.fn(),
  data: signal<OverviewEntity[]>([]),
  loading: signal(false)
};

const authGuardMock = () => {
  return Promise.resolve(true);
};

const refreshDataServiceMock = {
  teamFilterReady: new Subject(),
  quarterFilterReady: new Subject(),
  reloadOverviewSubject: new Subject(),
  okrBannerHeightSubject: new BehaviorSubject(5)
};

describe('OverviewComponent', () => {
  window.ResizeObserver =
    window.ResizeObserver ||
    jest.fn()
      .mockImplementation(() => ({
        disconnect: jest.fn(),
        observe: jest.fn(),
        unobserve: jest.fn()
      }));

  let component: OverviewComponent;
  let fixture: ComponentFixture<OverviewComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [
        AppRoutingModule,
        MatDialogModule,
        MatIconModule,
        MatMenuModule
      ],
      declarations: [OverviewComponent,
        ApplicationBannerComponent,
        ApplicationTopBarComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: OverviewService,
          useValue: overviewService
        },
        {
          provide: authGuard,
          useValue: authGuardMock
        },
        {
          provide: RefreshDataService,
          useValue: refreshDataServiceMock
        },
        {
          provide: MatDialogRef,
          useValue: {}
        },
        OAuthService,
        UrlHelperService,
        OAuthLogger,
        DateTimeProvider
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();

    fixture = TestBed.createComponent(OverviewComponent);
    component = fixture.componentInstance;

    overviewService.getOverview.mockReturnValue(of([overViewEntity1]));
    overviewService.data.set([overViewEntity1]);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should expose data from overviewService', () => {
    expect(component.data())
      .toEqual([overViewEntity1]);
  });
});
