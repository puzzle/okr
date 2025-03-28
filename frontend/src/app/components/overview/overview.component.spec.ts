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
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { FilterPageChange } from '../../shared/types/model/filter-page-change';

const overviewService = {
  getOverview: jest.fn()
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

const filterPage = {
  quarterId: 1,
  teamIds: [2,
    3],
  objectiveQueryString: 'test'
} as FilterPageChange;

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
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should call service method with correct params overview based on query-params', () => {
    jest.spyOn(overviewService, 'getOverview');
    jest.spyOn(component, 'loadOverview');
    component.loadOverview(filterPage);
    expect(overviewService.getOverview)
      .toHaveBeenCalledWith(1, [2,
        3], 'test');
  });

  it('should refresh overview entities after getOverview() is called', async() => {
    jest.spyOn(component.overviewEntities$, 'next');
    jest.spyOn(component, 'loadOverview');
    component.loadOverview(filterPage);
    expect(component.loadOverview)
      .toHaveBeenCalledTimes(1);
    expect(component.overviewEntities$.next)
      .toHaveBeenCalledWith([overViewEntity1]);
  });
  /*
   *
   * it('should get default if call throws error', async() => {
   *   overviewService.getOverview.mockReturnValue(of(new Error('')));
   *
   *   jest.spyOn(component, 'loadOverview');
   *   component.loadOverview(filterPage);
   *   expect(component.loadOverview)
   *     .toHaveBeenLastCalledWith();
   * });
   */
});
