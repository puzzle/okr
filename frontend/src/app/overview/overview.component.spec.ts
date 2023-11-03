import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverviewComponent } from './overview.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { overViewEntity1 } from '../shared/testData';
import { of } from 'rxjs';
import { OverviewService } from '../shared/services/overview.service';
import { AppRoutingModule } from '../app-routing.module';
import { RouterTestingHarness } from '@angular/router/testing';
import { authGuard } from '../shared/guards/auth.guard';

const overviewService = {
  getOverview: jest.fn(),
};

const authGuardMock = () => {
  return Promise.resolve(true);
};

describe('OverviewComponent', () => {
  let component: OverviewComponent;
  let fixture: ComponentFixture<OverviewComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AppRoutingModule],
      declarations: [OverviewComponent],
      providers: [
        {
          provide: OverviewService,
          useValue: overviewService,
        },
        {
          provide: authGuard,
          useValue: authGuardMock,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(OverviewComponent);
    component = fixture.componentInstance;
    overviewService.getOverview.mockReturnValue(of([overViewEntity1]));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  xit('should load default overview when no quarter is defined in route-params', () => {
    jest.spyOn(overviewService, 'getOverview');
    expect(overviewService.getOverview).toHaveBeenCalled();
  });

  xit('should load default overview on init', async () => {
    jest.spyOn(overviewService, 'getOverview');
    expect(overviewService.getOverview).toHaveBeenCalledWith(undefined, [], '');
  });

  it.each([
    ['?quarter=7', 7, [], ''],
    ['?teams=1,2', undefined, [1, 2], ''],
    ['?objectiveQuery=a%2520a', undefined, [], 'a a'],
    ['?teams=1,2&objectiveQuery=a%2520a', undefined, [1, 2], 'a a'],
    ['?teams=1,2&quarter=7', 7, [1, 2], ''],
    ['?quarter=7&objectiveQuery=a%2520a', 7, [], 'a a'],
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

  xit('should call loadOverviewWithParams', async () => {
    jest.spyOn(component, 'loadOverviewWithParams');
    const routerHarness = await RouterTestingHarness.create();

    await routerHarness.navigateByUrl('/?quarter=1');
    routerHarness.detectChanges();
    fixture.detectChanges();
    expect(component.loadOverviewWithParams).toHaveBeenCalledTimes(1);
  });

  it('should filter out the state param so it doesnt make the overview reload on login', async () => {
    jest.spyOn(component, 'loadOverviewWithParams');
    const routerHarness = await RouterTestingHarness.create();

    await routerHarness.navigateByUrl('/?state=asdf');
    routerHarness.detectChanges();
    fixture.detectChanges();
    expect(component.loadOverviewWithParams).toHaveBeenCalledTimes(0);
  });
});
