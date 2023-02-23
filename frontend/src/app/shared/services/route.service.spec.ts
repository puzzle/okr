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
  let activatedRoute: ActivatedRoute;
  let router: Router;

  const mockActivatedRoute = {
    queryParams: of({}),
  };
  beforeEach(() => {
    const mockRouter = {
      navigate: jest.fn(),
      events: of(
        new NavigationEnd(0, 'http://localhost:4200', 'http://localhost:4200')
      ),
      url: 'http://localhost:4200',
    };
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
    router = TestBed.inject(Router);
    location = TestBed.inject(Location);
    activatedRoute = TestBed.inject(ActivatedRoute);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add objective to selected objectives', () => {
    mockActivatedRoute.queryParams = of({ objectives: '1,2,3' });

    service.addToSelectedObjectives(4);

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: '1,2,3,4',
        keyresults: undefined,
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  });

  it('should remove objective from selected objectives', () => {
    mockActivatedRoute.queryParams = of({ objectives: '1,2,3,4' });

    service.removeFromSelectedObjectives(4);

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: '1,2,3',
        keyresults: undefined,
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  });

  it('should add keyresult to selected keyresults', () => {
    mockActivatedRoute.queryParams = of({ keyresults: '1,2,3' });

    service.addToSelectedKeyresults(4);

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: undefined,
        keyresults: '1,2,3,4',
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  });

  it('should remove keyresult from selected keyresults', () => {
    mockActivatedRoute.queryParams = of({ keyresults: '1,2,3,4' });

    service.removeFromSelectedKeyresult(4);

    expect(router.navigate).toHaveBeenCalledWith([], {
      relativeTo: mockActivatedRoute,
      queryParams: {
        objectives: undefined,
        keyresults: '1,2,3',
        quarterFilter: undefined,
        teamFilter: undefined,
      },
    });
  });

  it('should navigate to the given location with the current query params', () => {
    const queryParams = {
      objectives: '1,2,3',
      keyresults: '1',
      teamFilter: '1',
    };
    mockActivatedRoute.queryParams = of(queryParams);

    const location = '/some-location';
    service.navigate(location);

    expect(router.navigate).toHaveBeenCalledWith([location], {
      queryParams: queryParams,
    });
  });

  it('should navigate back to "/" when back() method is called', () => {
    mockActivatedRoute.queryParams = of({});
    const queryParams = {
      objectives: undefined,
      keyresults: undefined,
      teamFilter: undefined,
      quarterFilter: undefined,
    };

    service.back();
    expect(router.navigate).toHaveBeenCalledTimes(1);
    expect(router.navigate).toHaveBeenCalledWith(['/'], {
      queryParams: queryParams,
    });
  });

  it('should navigate to previousURL if previousUrl is available', () => {
    mockActivatedRoute.queryParams = of({});
    const queryParams = {
      objectives: undefined,
      keyresults: undefined,
      teamFilter: undefined,
      quarterFilter: undefined,
    };

    service.setPreviousUrl('http://localhost:4200/?objectives=1');
    const location = service.getPreviousUrl();
    service.back();
    expect(router.navigate).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith([location?.split('?')[0]], {
      queryParams: queryParams,
    });
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
