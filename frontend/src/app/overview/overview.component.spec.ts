import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverviewComponent } from './overview.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { overViewEntity1 } from '../shared/testData';
import { BehaviorSubject, of, Subject } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';
import { AppRoutingModule } from '../app-routing.module';
import { RouterTestingHarness } from '@angular/router/testing';
import { RefreshDataService } from '../shared/services/refresh-data.service';
import { authGuard } from '../shared/guards/auth.guard';
import { ApplicationBannerComponent } from '../application-banner/application-banner.component';
import { ApplicationTopBarComponent } from '../application-top-bar/application-top-bar.component';
import { DateTimeProvider, OAuthLogger, OAuthService, UrlHelperService } from 'angular-oauth2-oidc';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

const overviewService = {
  getOverview: jest.fn(),
};

const authGuardMock = () => {
  return Promise.resolve(true);
};

const refreshDataServiceMock = {
  teamFilterReady: new Subject(),
  quarterFilterReady: new Subject(),
  reloadOverviewSubject: new Subject(),
  okrBannerHeightSubject: new BehaviorSubject(5),
};

class ResizeObserverMock {
  observe() {}
  unobserve() {}
  disconnect() {}
}

describe('OverviewComponent', () => {
  //@ts-ignore
  global.ResizeObserver = ResizeObserverMock;

  let component: OverviewComponent;
  let fixture: ComponentFixture<OverviewComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AppRoutingModule, MatDialogModule, MatIconModule, MatMenuModule],
      declarations: [OverviewComponent, ApplicationBannerComponent, ApplicationTopBarComponent],
      providers: [
        {
          provide: OverviewService,
          useValue: overviewService,
        },
        {
          provide: authGuard,
          useValue: authGuardMock,
        },
        {
          provide: RefreshDataService,
          useValue: refreshDataServiceMock,
        },
        {
          provide: MatDialogRef,
          useValue: {},
        },
        OAuthService,
        UrlHelperService,
        OAuthLogger,
        DateTimeProvider,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(OverviewComponent);
    component = fixture.componentInstance;
    overviewService.getOverview.mockReturnValue(of({ overviews: [overViewEntity1], adminAccess: true }));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load default overview when no quarter is defined in route-params', () => {
    jest.spyOn(overviewService, 'getOverview');
    markFiltersAsReady();
    expect(overviewService.getOverview).toHaveBeenCalled();
  });

  it('should load default overview on init', async () => {
    jest.spyOn(overviewService, 'getOverview');
    markFiltersAsReady();
    expect(overviewService.getOverview).toHaveBeenCalledWith(undefined, [], '');
  });

  it.each([
    ['?quarter=7', 7, [], ''],
    ['?teams=1,2', undefined, [1, 2], ''],
    ['?objectiveQuery=a%20a', undefined, [], 'a a'],
    ['?teams=1,2&objectiveQuery=a%20a', undefined, [1, 2], 'a a'],
    ['?teams=1,2&quarter=7', 7, [1, 2], ''],
    ['?quarter=7&objectiveQuery=a%20a', 7, [], 'a a'],
  ])(
    'should load overview based on queryparams',
    async (query: string, quarterParam?: number, teamsParam?: number[], objectiveQueryParam?: string) => {
      jest.spyOn(overviewService, 'getOverview');
      jest.spyOn(component, 'loadOverview');
      const routerHarness = await RouterTestingHarness.create();
      await routerHarness.navigateByUrl('/' + query);
      routerHarness.detectChanges();
      component.loadOverviewWithParams();
      expect(overviewService.getOverview).toHaveBeenCalledWith(quarterParam, teamsParam, objectiveQueryParam);
      expect(component.loadOverview).toHaveBeenCalledWith(quarterParam, teamsParam, objectiveQueryParam);
    },
  );

  it('should refresh overview Entities after getOverview is called', async () => {
    jest.spyOn(component.overviewEntities$, 'next');
    jest.spyOn(component, 'loadOverview');
    component.loadOverview();
    expect(component.loadOverview).toHaveBeenCalledTimes(1);
    expect(component.overviewEntities$.next).toHaveBeenCalledWith([overViewEntity1]);
  });

  it('should get default if call throws error', async () => {
    overviewService.getOverview.mockReturnValue(of(new Error('')));

    jest.spyOn(component, 'loadOverview');
    component.loadOverview();
    expect(component.loadOverview).toHaveBeenLastCalledWith();
  });

  function markFiltersAsReady() {
    refreshDataServiceMock.quarterFilterReady.next(null);
    refreshDataServiceMock.teamFilterReady.next(null);
    fixture.detectChanges();
  }
});
