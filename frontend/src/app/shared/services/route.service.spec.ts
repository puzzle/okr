import { fakeAsync, TestBed, tick } from '@angular/core/testing';
import { Location } from '@angular/common';
import { RouteService } from './route.service';
import {
  ActivatedRoute,
  convertToParamMap,
  NavigationEnd,
  Router,
} from '@angular/router';
import { of } from 'rxjs';

describe('RouteService', () => {
  let service: RouteService;
  let location: Location;
  let router: Router;
  let activatedRoute: ActivatedRoute;

  const mockActivatedRoute = {
    queryParams: of({}),
  };

  const mockRouter = {
    navigate: jest.fn(),
    events: of(
      new NavigationEnd(0, 'http://localhost:4200', 'http://localhost:4200')
    ),
    url: 'http://localhost:4200',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        RouteService,
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of(
              convertToParamMap({ objectives: '1,2', keyresults: '1' })
            ),
          },
        },
        { provide: Location, useValue: location },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: Router, useValue: mockRouter },
      ],
    });
    service = TestBed.inject(RouteService);
    location = TestBed.inject(Location);
    router = TestBed.inject(Router);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add objective to selected objectives', fakeAsync(() => {
    mockActivatedRoute.queryParams = of({ objectives: '1,2,3' });

    service.addToSelectedObjectives(4);

    tick();

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: '1,2,3,4',
        keyresults: undefined,
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  }));

  it('should remove objective from selected objectives', fakeAsync(() => {
    mockActivatedRoute.queryParams = of({ objectives: '1,2,3,4' });

    service.removeFromSelectedObjectives(4);

    tick();

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: '1,2,3',
        keyresults: undefined,
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  }));

  it('should add keyresult to selected keyresults', fakeAsync(() => {
    mockActivatedRoute.queryParams = of({ keyresults: '1,2,3' });

    service.addToSelectedKeyresults(4);

    tick();

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: undefined,
        keyresults: '1,2,3,4',
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  }));

  it('should remove keyresult from selected keyresults', fakeAsync(() => {
    mockActivatedRoute.queryParams = of({ keyresults: '1,2,3,4' });

    service.removeFromSelectedKeyresult(4);

    tick();

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: undefined,
        keyresults: '1,2,3',
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  }));

  it('should navigate to the given location with the current query params', fakeAsync(() => {
    const queryParams = {
      objectives: '1,2,3',
      keyresults: '1',
      teamFilter: '1',
    };
    mockActivatedRoute.queryParams = of(queryParams);

    const location = '/some-location';
    service.navigate(location);

    tick();

    expect(router.navigate).toHaveBeenCalledWith([location], {
      queryParams: queryParams,
    });
  }));

  xit('should call location.back()', () => {
    const spy = jest.spyOn(service, 'back');
    const routeService = new RouteService(location, activatedRoute, router);
    routeService.back();
    expect(spy).toHaveBeenCalled();
  });

  xit('should change quarterfilter', fakeAsync(() => {
    mockActivatedRoute.queryParams = of(
      { objectives: undefined },
      { keyresult: undefined },
      { quarterFilter: '2' },
      { teamFilter: undefined }
    );

    service.changeQuarterFilter(1);

    tick();

    expect(router.navigate).toBe(activatedRoute);
  }));
});
